import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Library Login");
        title.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 24;");
        layout.getChildren().add(title);

        layout.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField
        );

        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(200);
        loginButton.setOnAction(e -> validateLogin());

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);
        registerBtn.setOnAction(e -> {
            UserRegister registerView = new UserRegister(stage);
            registerView.initializeComponents();
        });

        layout.getChildren().addAll(loginButton, registerBtn);

        loginScene = new Scene(layout, 500, 400);
        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User loggedInUser = AuthenticationService.authenticate(username, password);
        if(loggedInUser != null){
            if(AuthorizationService.isStaff(loggedInUser)){
                StaffView staffView = new StaffView(stage, loggedInUser);
                staffView.initializeComponents();
            } else if (AuthorizationService.isUser(loggedInUser)) {
                CustomerView customerView = new CustomerView(stage, loggedInUser);
                customerView.initializeComponents();
            } else{
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