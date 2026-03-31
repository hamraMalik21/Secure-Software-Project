import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {
    private Scene loginScene;
    private Stage stage;

    public MainView(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Library Home Page");
        title.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 24;");
        layout.getChildren().add(title);

        Button showBooksBtn = new Button("Show Library Books");
        showBooksBtn.setPrefWidth(200);
        Button loginButton = new Button("Log In");
        loginButton.setPrefWidth(200);
        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(200);

        showBooksBtn.setOnAction(e -> {
            ShowAllBooks showAllBooks = new ShowAllBooks(stage);
            showAllBooks.initializeComponents();
        });
        loginButton.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });
        registerBtn.setOnAction(e -> {
            UserRegister registerView = new UserRegister(stage);
            registerView.initializeComponents();
        });

        layout.getChildren().addAll(showBooksBtn, loginButton, registerBtn);

        loginScene = new Scene(layout, 500, 400);
        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }


}