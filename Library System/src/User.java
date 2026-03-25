public class User {

    private int user_id;
    private String username;
    private String passwordHash;
    private String role;


    public User(String role, String passwordHash, String username) {
        this.role = role;
        this.passwordHash = passwordHash;
        this.username = username;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }
}