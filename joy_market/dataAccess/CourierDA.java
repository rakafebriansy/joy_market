package joy_market.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import joy_market.core.DBConnection;
import joy_market.models.Courier;

public class CourierDA {
    // Get a courier from the database by matching email and password
    public static Courier getCourierByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM couriers WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set the email and password parameters
            stmt.setString(1, email);
            stmt.setString(2, password);
            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            // If a matching record is found, return a Courier object
            if (rs.next()) {
                return new Courier(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // Print the error message if something goes wrong
            e.printStackTrace();
        }
        // Return null if no courier is found
        return null;
    }
    
    // Update courier information (email and password) in the database
    public static boolean updateCourier(Courier courier) {
        String sql = "UPDATE couriers SET email=?, password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set new values for the courier
            stmt.setString(1, courier.getEmail());
            stmt.setString(2, courier.getPassword());
            stmt.setInt(3, courier.getId());
            // Execute the update and return true if successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Print error details if update fails
            e.printStackTrace();
            return false;
        }
    }
    // Get a list of all couriers from the database
    public static List<Courier> getAllCouriers() {
        List<Courier> couriers = new ArrayList<>();
        String sql = "SELECT id, email, password FROM couriers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through each record and create Courier objects
            while (rs.next()) {
                Courier c = new Courier(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                couriers.add(c);
            }
        } catch (SQLException e) {
            // Print error details if something goes wrong
            e.printStackTrace();
        }
        // Return the list of all couriers
        return couriers;
    }

}
