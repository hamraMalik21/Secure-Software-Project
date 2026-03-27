import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IssueReturnView {
    private User currentUser;
    private Stage stage;

    public IssueReturnView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Issue/Return Books");
        title.setFont(new Font("Times New Roman",24));


        Label customerName = new Label("Enter Customer Name: ");
        Label bookName = new Label("Enter Book Name: ");

        TextField customerField = new TextField();
        customerField.setPromptText("Customer Name");
        TextField bookField = new TextField();
        bookField.setPromptText("Book Name");

        Button issueBtn = new Button("Issue Book");
        issueBtn.setPrefWidth(95);

        Button returnBtn = new Button("Return Book");
        returnBtn.setPrefWidth(95);

        HBox issueReturnLayout = new HBox(issueBtn, returnBtn);
        issueReturnLayout.setSpacing(10);


        Button bookStatsBtn = new Button("View Book Status");
        bookStatsBtn.setPrefWidth(200);


        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(95);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(95);

        HBox buttonLayout = new HBox(logoutBtn, backBtn);
        buttonLayout.setSpacing(10);

        // Actions *****
        issueBtn.setOnAction(e -> {
            /*
            Check if fields are empty
            Check regex
            Add logic to check if book exists
            Add logic to check if customer exists
            If both exist, add logic to check if book is available
            If true, add book/customer to borrow with today's date
             */
            System.out.println("Issue Book");
        });

        returnBtn.setOnAction(e -> {
            /*
            Check for empty fields
            Check regex
            Add logic to see if customer exists
            Add logic to see if book exists
            Add logic to check if book is in borrowed_book database and this customer borrowed this book
            Check date, if date exceeds book rental period then -> ?
            If all true, remove borrowed book from borrowed_book database

             */
            System.out.println("Return Book");


        });

        bookStatsBtn.setOnAction(e -> {
            System.out.println("Update Book");
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
        StaffLayout.getChildren().addAll(title,customerName,customerField,bookName, bookField, issueReturnLayout, buttonLayout );


        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}
