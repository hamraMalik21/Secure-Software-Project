import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// this class does checks from the database
public class BookCheck {
    // Check if movie exists
    public static boolean bookExists(String book_name) {
        String query = "SELECT * FROM book WHERE book_name = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, book_name);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Check if movie has a showtime
    public static boolean isBorrowed(String book_name) {
        String query = "SELECT * FROM borrowed_book WHERE book_name = ?";

        try {
            Connection con = DBUtils.establishConnection();
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, book_name);

            ResultSet rs = stmt.executeQuery();

            boolean exists = rs.next();

            rs.close();
            DBUtils.closeConnection(con, stmt);

            return exists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
