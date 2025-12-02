package joy_market.dataAccess;

import joy_market.core.DBConnection;
import joy_market.models.Customer;
import java.sql.*;

public class CustomerDA {

    // Check if an email already exists in the 'users' table
    public static boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            // If count > 0, then email already exists
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return false if not found or error occurs
        return false;
    }
    
    // Save a new customer (user) into the database
    public static boolean saveDA(Customer user) {
        String sql = "INSERT INTO users (email, password, full_name, phone, address, gender, balance) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set user information
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getGender().toUpperCase());
            stmt.setLong(7, user.getBalance());
            
            // Execute the insert query
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if saving fails
    }
    
    // Get a customer by matching email and password (used for login)
    public static Customer getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            // If a record is found, return a Customer object
            if (rs.next()) {
                return new Customer(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("gender"),
                    rs.getLong("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if login fails
    }
    
    // Update user information in the database
    public static boolean updateUser(Customer user) {
        String sql = "UPDATE users SET full_name=?, email=?, phone=?, address=?, gender=?, password=?, balance=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set updated user details
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getPassword());
            stmt.setLong(7, user.getBalance());
            stmt.setInt(8, user.getId());
            
            // Execute the update query
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a user by their ID
    public static Customer getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            // If a record is found, create and return a Customer object
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getLong("balance")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user found
    }
    
    // Get only the user's full name by ID
    public static String getUserNameById(int id) {
        Customer user = getUserById(id);
        return (user != null) ? user.getFullName() : "Unknown";
    }

}
