
import java.sql.*;
import java.util.concurrent.locks.ReentrantLock;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class DatabaseUtil {
    private static final ReentrantLock bookingLock = new ReentrantLock();
    
    public static boolean authenticateUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        
        Database conn = new Database();
        try {
             PreparedStatement pst = conn.con.prepareStatement(query);
            
            pst.setString(1, email);
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
     public static synchronized String processBooking(String username, String movie, 
        String theater, String date, String time, int tickets) {
    bookingLock.lock(); // ReentrantLock for thread safety
    Database conn = new Database();
        try {
        conn.con.setAutoCommit(false); // Start transaction
        // 2. Calculate fare
        int fare = tickets * 200;

        // 3. Save booking to database
        String query = "INSERT INTO bookings (username, movie, theater, date, time, tickets, fare) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.con.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, movie);
            pst.setString(3, theater);
            pst.setString(4, date);
            pst.setString(5, time);
            pst.setInt(6, tickets);
            pst.setInt(7, fare);
            
            pst.executeUpdate();
            conn.con.commit(); // Commit transaction
            return "BOOKING_SUCCESS:Booking confirmed. Fare: " + fare;
        } catch (SQLException e) {
            conn.con.rollback(); // Rollback on failure
            return "BOOKING_FAILED:Database error occurred";
        }
    } catch (SQLException e) {
        return "BOOKING_FAILED:Database error occurred";
    } finally {
        bookingLock.unlock();
    }}
    
    public static synchronized boolean saveBooking(String username, String movie, String theater, String date, String time, int tickets) {
        bookingLock.lock();
        Database conn = new Database();
        try {
            String query = "INSERT INTO bookings (username, movie, theater, date, time, tickets) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.con.prepareStatement(query)) {
                pst.setString(1, username);
                pst.setString(2, movie);
                pst.setString(3, theater);
                pst.setString(4, date);
                pst.setString(5, time);
                pst.setInt(6, tickets);

                return pst.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
          catch(Exception e){
                  e.getStackTrace();
                  
        } finally {
            bookingLock.unlock();
        }
        return false;
    }

}