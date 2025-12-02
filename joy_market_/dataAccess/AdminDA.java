package joy_market.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import joy_market.core.DBConnection;
import joy_market.models.Admin;

public class AdminDA {
    public static Admin getAdminByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
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
    
    public static boolean updateAdmin(Admin admin) {
        StringBuilder sql = new StringBuilder("UPDATE admins SET email=?");
        boolean updatePassword = (admin.getPassword() != null && !admin.getPassword().isEmpty());

        if (updatePassword) sql.append(", password=?");
        sql.append(" WHERE id=?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            stmt.setString(1, admin.getEmail());

            int index = 2;
            if (updatePassword) {
                stmt.setString(index++, admin.getPassword());
            }

            stmt.setInt(index, admin.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
