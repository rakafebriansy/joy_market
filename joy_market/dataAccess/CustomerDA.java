package joy_market.dataAccess;


import joy_market.core.DBConnection;
import joy_market.models.Customer;
import joy_market.models.User;

import java.sql.*;

public class CustomerDA {
    private final UserDA userDA = new UserDA();

    public boolean insertCustomerWithUser(Customer customer, User user) throws SQLException {
        String sql = "INSERT INTO customers (user_id, full_name, phone, address, gender, balance) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);

                int userId = userDA.insertUser(user);
                if (userId <= 0) throw new SQLException("Gagal menyimpan data user.");

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, userId);
                    stmt.setString(2, customer.getFullName());
                    stmt.setString(3, customer.getPhone());
                    stmt.setString(4, customer.getAddress());
                    stmt.setString(5, customer.getGender());
                    stmt.setDouble(6, customer.getBalance());
                    stmt.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Customer getCustomerByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getDouble("balance")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET full_name=?, phone=?, address=?, gender=?, balance=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getGender());
            stmt.setDouble(5, customer.getBalance());
            stmt.setInt(6, customer.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
