package joy_market.dataAccess;

import joy_market.core.DBConnection;
import joy_market.models.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDA {
    
    // Get the cart ID for a specific user
    // If the user has no cart, create a new one
    public static int getCartIdByUser(int userId) {
        String sql = "SELECT id FROM carts WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Create a new cart if none exists
        return createCartItem(userId);
    }
    
    // Create a new cart for a user and return the new cart ID
    private static int createCartItem(int userId) {
        String sql = "INSERT INTO carts(user_id) VALUES(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if creation fails
    }
    
    // Add a product to the user's cart
    // If the product already exists in the cart, increase its count
    public static boolean saveDA(int userId, int productId, int count) {
        int cartId = getCartIdByUser(userId);

        String checkSql = "SELECT id, count FROM cart_items WHERE cart_id=? AND product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, cartId);
            checkStmt.setInt(2, productId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // If product already exists, update its count
                    int existingId = rs.getInt("id");
                    int existingCount = rs.getInt("count");
                    String updateSql = "UPDATE cart_items SET count=? WHERE id=?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, existingCount + count);
                        updateStmt.setInt(2, existingId);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // If product doesn't exist, insert a new one
                    String insertSql = "INSERT INTO cart_items(cart_id, product_id, count) VALUES(?,?,?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, cartId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, count);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if failed
    }
    
    // Get all cart items belonging to a specific user
    public static List<CartItem> getCartItemsByUser(int userId) {
        List<CartItem> items = new ArrayList<>();
        int cartId = getCartIdByUser(userId);
        String sql = "SELECT * FROM cart_items WHERE cart_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Create a CartItem object for each record
                    CartItem ci = new CartItem(
                            rs.getInt("id"),
                            rs.getInt("cart_id"),
                            rs.getInt("product_id"),
                            rs.getInt("count")
                    );
                    items.add(ci);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items; // Return list of cart items
    }
    
    // Delete all items in a user's cart
    public static boolean clearCart(int userId) {
        int cartId = getCartIdByUser(userId);
        String sql = "DELETE FROM cart_items WHERE cart_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete a specific item from the cart by its ID
    public static boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            return stmt.executeUpdate() > 0; // Return true if delete successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
