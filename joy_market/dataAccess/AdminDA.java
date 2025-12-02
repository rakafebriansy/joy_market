package joy_market.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import joy_market.core.DBConnection;
import joy_market.models.Admin;

public class AdminDA {
    // This method is used to get an admin from the database by matching email and password
    public static Admin getAdminByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters for the SQL query
            stmt.setString(1, email);
            stmt.setString(2, password);
            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            // If a record is found, create and return an Admin object
            if (rs.next()) {
                return new Admin(
                	rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // Print error details if something goes wrong
            e.printStackTrace();
        }
        return null; // Return null if no admin is found
    }
    
    // This method updates the admin's information in the database
    public static boolean updateAdmin(Admin admin) {
        // Build SQL query dynamically
        StringBuilder sql = new StringBuilder("UPDATE admins SET email=?");
        boolean updatePassword = (admin.getPassword() != null && !admin.getPassword().isEmpty());
        
        // Add password update only if it’s not empty
        if (updatePassword) sql.append(", password=?");
        sql.append(" WHERE id=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            // Set email value
            stmt.setString(1, admin.getEmail());

            int index = 2;
            // Set password value only if it's updated
            if (updatePassword) {
                stmt.setString(index++, admin.getPassword());
            }
            
            // Set admin ID for WHERE condition
            stmt.setInt(index, admin.getId());
            
            // Execute the update query and return true if successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Print error details if update fails
            e.printStackTrace();
            return false;
        }
    }

}
