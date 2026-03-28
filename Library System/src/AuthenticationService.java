import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthenticationService {
    // Authenticate user
    public static User authenticate(String username, String suppliedPassword){
        Connection con = DBUtils.establishConnection();
        String query = "SELECT * FROM user WHERE username = ?;";
        User loggedInUser = null;

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // check password
                boolean correctPassword = BCrypt.checkpw(suppliedPassword, storedPassword);
                if (correctPassword){
                    loggedInUser = new User(
                            rs.getString("role"),
                            storedPassword,
                            rs.getString("username")
                    );
                }
            }
            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return loggedInUser;
    }

    // Check if username exists
    public static boolean userExists(String username){
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT * FROM user WHERE username = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            DBUtils.closeConnection(con, stmt);
            return exists;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Register new user
    public static void register(String username, String passwordHash, String role){
        try{
            Connection con = DBUtils.establishConnection();
            String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, role);
            stmt.executeUpdate();
            DBUtils.closeConnection(con, stmt);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}