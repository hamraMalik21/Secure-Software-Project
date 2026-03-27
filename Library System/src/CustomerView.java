import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CustomerView {
    private User currentUser;
    private Stage stage;

    public CustomerView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Welcome Customer");
        title.setFont(new Font("Times New Roman",24));


        Button viewBookBtn = new Button("View All Books");
        viewBookBtn.setPrefWidth(200);

        Button borrowingHistoryBtn = new Button("Show Borrowing History");
        borrowingHistoryBtn.setPrefWidth(200);


        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        viewBookBtn.setOnAction(e -> {
            System.out.println("View Books");
        });

        borrowingHistoryBtn.setOnAction(e -> {
            System.out.println("View Borrowing History");


        });


        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,viewBookBtn,borrowingHistoryBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}
