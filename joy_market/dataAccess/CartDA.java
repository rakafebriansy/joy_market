package joy_market.dataAccess;

import joy_market.core.DBConnection;
import joy_market.models.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDA {

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
        return createCartItem(userId);
    }

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
        return -1;
    }

    public static boolean saveDA(int userId, int productId, int count) {
        int cartId = getCartIdByUser(userId);

        String checkSql = "SELECT id, count FROM cart_items WHERE cart_id=? AND product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, cartId);
            checkStmt.setInt(2, productId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("id");
                    int existingCount = rs.getInt("count");
                    String updateSql = "UPDATE cart_items SET count=? WHERE id=?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, existingCount + count);
                        updateStmt.setInt(2, existingId);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
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
        return false;
    }

    public static List<CartItem> getCartItemsByUser(int userId) {
        List<CartItem> items = new ArrayList<>();
        int cartId = getCartIdByUser(userId);
        String sql = "SELECT * FROM cart_items WHERE cart_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
        return items;
    }

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
    
    public static boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
