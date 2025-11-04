package joy_market.dataAccess;

import joy_market.core.DBConnection;
import joy_market.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDA {

    public static boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (name, price, stock, is_fresh, description) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setLong(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.setBoolean(4, p.isFresh());
            stmt.setString(5, p.getDescription());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateProduct(Product p) {
    	System.out.println(p.getId().intValue());
        String sql = "UPDATE products SET name=?, price=?, stock=?, is_fresh=?, description=? WHERE id=?";
        System.out.println("Updating product: " + p.getId());
        System.out.println("Name: " + p.getName());
        System.out.println("Price: " + p.getPrice());
        System.out.println("Stock: " + p.getStock());
        System.out.println("Fresh: " + p.isFresh());
        System.out.println("Desc: " + p.getDescription());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setLong(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.setBoolean(4, p.isFresh());
            stmt.setString(5, p.getDescription());
            stmt.setInt(6, p.getId().intValue());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getLong("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("is_fresh"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("price"),
                            rs.getInt("stock"),
                            rs.getBoolean("is_fresh"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean updateProductStock(int productId, int newStock) {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
