import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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


        TextField searchField = new TextField();
        searchField.setPromptText("Enter Book");

        Button checkBtn = new Button("Check Availibility");

        HBox checkLayout = new HBox(searchField, checkBtn);


        Button viewBookBtn = new Button("View All Books");
        viewBookBtn.setPrefWidth(200);

        Button borrowingHistoryBtn = new Button("Show Borrowing History");
        borrowingHistoryBtn.setPrefWidth(200);


        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(200);

        // Actions *****
        viewBookBtn.setOnAction(e -> {
            ShowAllBooks showAllBooks = new ShowAllBooks(stage, currentUser);
            showAllBooks.initializeComponents();
        });

        borrowingHistoryBtn.setOnAction(e -> {

            ShowBorrowedBooks showBorrowedBooks = new ShowBorrowedBooks(stage, currentUser);
            showBorrowedBooks.initializeComponents();

        });

        checkBtn.setOnAction(e -> {
            /*
            Check empty field
            Check regex
            Check if book exists in book database -> Alert book doesn't exist
            Check if book exists in borrowed_book database -> Alert book already borrowed
            Check if book exist in book database and does not exist in borrowed_book database -> book available
             */
            System.out.println("Check Book Availibility");


        });


        // Logout --> return to UserLogin
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        // Layout
        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,checkLayout, viewBookBtn,borrowingHistoryBtn,logoutBtn);

        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}
