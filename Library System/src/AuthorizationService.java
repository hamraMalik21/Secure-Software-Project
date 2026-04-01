public class AuthorizationService {
    public static boolean isStaff(User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("Staff");
    }
    public static boolean isUser(User currentUser) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase("Member");
    }
}