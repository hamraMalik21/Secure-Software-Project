import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

            String bookName = searchField.getText().trim();

            if (bookName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a book name.");
                alert.showAndWait();
                return;
            }

            if (!bookName.matches("[a-zA-Z0-9\\s]+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid book name.");
                alert.showAndWait();
                return;
            }

            try {
                Connection con = DBUtils.establishConnection();

                String bookQuery = "SELECT * FROM book WHERE book_name = ?";
                PreparedStatement bookStmt = con.prepareStatement(bookQuery);
                bookStmt.setString(1, bookName);
                ResultSet bookRs = bookStmt.executeQuery();

                if (!bookRs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Book doesn't exist.");
                    alert.showAndWait();

                    bookStmt.close();
                    con.close();
                    return;
                }

                String borrowedQuery = "SELECT * FROM borrowed_book WHERE book_name = ?";
                PreparedStatement borrowedStmt = con.prepareStatement(borrowedQuery);
                borrowedStmt.setString(1, bookName);
                ResultSet borrowedRs = borrowedStmt.executeQuery();

                if (borrowedRs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Book already borrowed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Book is available.");
                    alert.showAndWait();
                }

                bookStmt.close();
                borrowedStmt.close();
                con.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

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