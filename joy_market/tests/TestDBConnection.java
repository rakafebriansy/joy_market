package joy_market.tests;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/joy_market?useSSL=false&serverTimezone=UTC",
                "root", ""
            );
            System.out.println("✅ Koneksi berhasil!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
