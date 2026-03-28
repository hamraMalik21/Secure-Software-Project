import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class UserRegister {
    private Stage stage;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private PasswordField confirmPasswordField = new PasswordField();
    private ComboBox<String> roleBox = new ComboBox<>();

    public UserRegister(Stage primaryStage){
        this.stage = primaryStage;
    }

    public void initializeComponents(){
        Label title = new Label("Register New User");
        title.setFont(new Font("Times New Roman", 24));

        roleBox.getItems().addAll("user", "staff");
        roleBox.setValue("user");
        roleBox.setPrefWidth(200);

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);
        registerBtn.setOnAction(e -> registerUser());

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        VBox formLayout = new VBox(15);
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Confirm Password:"), confirmPasswordField,
                new Label("Select Role:"), roleBox
        );

        HBox buttonsLayout = new HBox(20);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(registerBtn, backBtn);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getChildren().addAll(title, formLayout, buttonsLayout);

        Scene scene = new Scene(mainLayout, 500, 400);
        stage.setTitle("Register User");
        stage.setScene(scene);
        stage.show();
    }

    private void registerUser(){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleBox.getValue();

        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        if(!password.equals(confirmPassword)){
            showAlert("Input Error", "Passwords do not match.");
            return;
        }

        if(AuthenticationService.userExists(username)){
            showAlert("Error", "Username already exists.");
            return;
        }

        try{
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            AuthenticationService.register(username, hashedPassword, role);
            showSuccess("Success", "User registered successfully.");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}