package joy_market.dataAccess;

import java.sql.*;
import java.util.*;

import joy_market.core.DBConnection;
import joy_market.models.Order;

public class OrderHeaderDA {
    
    // Get all order records from the 'orders' table
    public static List<Order> getOrderHeader() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through each record and create Order objects
            while (rs.next()) {
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getBoolean("promo"),
                    rs.getLong("total_price"),
                    rs.getString("status"),
                    (Integer) rs.getObject("courier_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Assign a courier to an order and change its status from PENDING to IN_PROGRESS
    public static boolean assignCourier(int orderId, int courierId) {
        String sql = "UPDATE orders SET courier_id=?, status='IN_PROGRESS' WHERE id=? AND status='PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courierId);
            stmt.setInt(2, orderId);
            
            // Return true if at least one row is updated
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Insert a new order into the 'orders' table and return the generated order ID
    public static int insertOrder(int userId, long totalPrice) {
        String sql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setLong(2, totalPrice);
            int affectedRows = stmt.executeUpdate();
            
            // If no rows affected, throw an error
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            // Get the auto-generated order ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // return generated order ID
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Insert a product item into the 'order_items' table (part of the order)
    public static boolean insertOrderItem(int orderId, int productId, int count, long price) {
        String sql = "INSERT INTO order_items (order_id, product_id, count, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, count);
            stmt.setLong(4, price);
            
            // Return true if the insert was successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all orders belonging to a specific user, ordered by creation date (newest first)
    public static List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            // Create an Order object for each record found
            while (rs.next()) {
                Order o = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("promo"),
                        rs.getLong("total_price"),
                        rs.getString("status"),
                        rs.getInt("courier_id")
                );
                orders.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update the delivery status of an order (e.g., from IN_PROGRESS to DELIVERED)
    public static boolean editDeliveryStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all orders assigned to a specific courier
    public static List<Order> getOrdersByCourierId(int courierId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE courier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courierId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getBoolean("promo"),
                            rs.getLong("total_price"),
                            rs.getString("status"),
                            rs.getObject("courier_id") != null ? rs.getInt("courier_id") : null
                    );
                    orders.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
