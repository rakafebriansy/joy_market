package joy_market.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import joy_market.core.DBConnection;
import joy_market.models.Courier;

public class CourierDA {
    public static Courier getCourierByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM couriers WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Courier(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean updateCourier(Courier courier) {
        String sql = "UPDATE couriers SET email=?, password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courier.getEmail());
            stmt.setString(2, courier.getPassword());
            stmt.setInt(3, courier.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
