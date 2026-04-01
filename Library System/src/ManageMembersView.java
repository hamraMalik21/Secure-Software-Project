import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ManageMembersView {
    private User currentUser;
    private Stage stage;

    public ManageMembersView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Manage Members");
        title.setFont(new Font("Times New Roman",24));


        Button deleteMemberBtn = new Button("Delete Member");
        deleteMemberBtn.setPrefWidth(200);


        Button showMemberBtn = new Button("Show All Members");
        showMemberBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(95);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(95);

        HBox buttonLayout = new HBox(logoutBtn, backBtn);
        buttonLayout.setSpacing(10);

        // Actions *****

        deleteMemberBtn.setOnAction(e -> {
            DeleteMember deleteMember = new DeleteMember(stage, currentUser);
            deleteMember.show();
            System.out.println("Testing if button works");
        });


        showMemberBtn.setOnAction(e -> {
            ShowAllMembers showAllMembers = new ShowAllMembers(stage, currentUser);
            showAllMembers.initializeComponents();
        });

        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });
        backBtn.setOnAction(e -> {
            StaffView staffView = new StaffView(stage, currentUser);
            staffView.initializeComponents();
        });

        // Layout
        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,deleteMemberBtn,showMemberBtn, buttonLayout);

        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}