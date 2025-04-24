
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    Connection con;

    public Database() {
        try {
            final String url = "jdbc:mysql://localhost:3306/MovieBooking";  
            final String root = "root";
            final String password = "Shalini@123";

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, root, password);
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}