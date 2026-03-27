import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;

public class StaffView {
    private User currentUser;
    private Stage stage;

    public StaffView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Welcome Library Staff");
        title.setFont(new Font("Times New Roman",24));


        Button manageBooksBtn = new Button("Manage Books");
        manageBooksBtn.setPrefWidth(200);

        Button issueReturnBtn = new Button("Issue/Return Books");
        issueReturnBtn.setPrefWidth(200);

        Button fineBtn = new Button("Manage Fines");
        fineBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        manageBooksBtn.setOnAction(e -> {
            ManageBooksView manageBooksView = new ManageBooksView(stage, currentUser);
            manageBooksView.initializeComponents();
        });

        issueReturnBtn.setOnAction(e -> {
            IssueReturnView issueReturnView = new IssueReturnView(stage, currentUser);
            issueReturnView.initializeComponents();


        });

        fineBtn.setOnAction(e -> {
            System.out.println("Fine Management");
        });

        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,manageBooksBtn,issueReturnBtn,fineBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}
