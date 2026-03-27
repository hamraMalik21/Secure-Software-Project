import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));
        Button loginButton = new Button("Sign In");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                validateLogin();
            }
        });
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton);

        loginScene = new Scene(loginLayout, 600, 600);
        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Authenticate the user using the authentication service module
        User loggedInUser = AuthenticationService.authenticate(username, password);
        if(loggedInUser != null){
            // Authorization, grant the logged-in user access based on the role
            if(AuthorizationService.isStaff(loggedInUser)){
                StaffView staffView = new StaffView(stage, loggedInUser);
                //System.out.println(BCrypt.hashpw("customer", BCrypt.gensalt(12)));
                staffView.initializeComponents();

            } else if (AuthorizationService.isUser(loggedInUser)) {

                CustomerView customerView = new CustomerView(stage, loggedInUser);
                customerView.initializeComponents();
            }else{

                showAlert("Authentication Failed", "Invalid username or password.");
            }
        }else{
            showAlert("Authentication Failed", "Invalid username or password.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
