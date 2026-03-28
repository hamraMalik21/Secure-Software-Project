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
import java.time.LocalDate;

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
            String customerNameText = customerField.getText().trim();
            String bookNameText = bookField.getText().trim();

            if (customerNameText.isEmpty() || bookNameText.isEmpty()) {
                showAlert("Input Error", "Please fill in all fields.");
                return;
            }

            if (!customerNameText.matches("[a-zA-Z0-9\\s]+") || !bookNameText.matches("[a-zA-Z0-9\\s]+")) {
                showAlert("Input Error", "Invalid input.");
                return;
            }

            try {
                Connection con = DBUtils.establishConnection();

                String bookQuery = "SELECT * FROM book WHERE book_name = ?";
                PreparedStatement bookStmt = con.prepareStatement(bookQuery);
                bookStmt.setString(1, bookNameText);
                ResultSet bookRs = bookStmt.executeQuery();

                if (!bookRs.next()) {
                    showInfo("Issue Book", "Book does not exist.");
                    bookStmt.close();
                    con.close();
                    return;
                }

                String customerQuery = "SELECT * FROM user WHERE username = ? AND role = 'user'";
                PreparedStatement customerStmt = con.prepareStatement(customerQuery);
                customerStmt.setString(1, customerNameText);
                ResultSet customerRs = customerStmt.executeQuery();

                if (!customerRs.next()) {
                    showInfo("Issue Book", "Customer does not exist.");
                    bookStmt.close();
                    customerStmt.close();
                    con.close();
                    return;
                }

                String borrowedQuery = "SELECT * FROM borrowed_book WHERE book_name = ?";
                PreparedStatement borrowedStmt = con.prepareStatement(borrowedQuery);
                borrowedStmt.setString(1, bookNameText);
                ResultSet borrowedRs = borrowedStmt.executeQuery();

                if (borrowedRs.next()) {
                    showInfo("Issue Book", "Book is already borrowed.");
                    bookStmt.close();
                    customerStmt.close();
                    borrowedStmt.close();
                    con.close();
                    return;
                }

                String insertQuery = "INSERT INTO borrowed_book (customer_name, book_name, date) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, customerNameText);
                insertStmt.setString(2, bookNameText);
                insertStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));

                int rows = insertStmt.executeUpdate();

                if (rows > 0) {
                    showInfo("Issue Book", "Book issued successfully.");
                    customerField.clear();
                    bookField.clear();
                }

                bookStmt.close();
                customerStmt.close();
                borrowedStmt.close();
                insertStmt.close();
                con.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        returnBtn.setOnAction(e -> {
            /*
            Check for empty fields
            Check regex
            Add logic to see if customer exists
            Add logic to see if book exists
            Add logic to check if book is in borrowed_book database and this customer borrowed this book
            Check date, if date exceeds book rental period then -> automatically add fine
            If all true, remove borrowed book from borrowed_book database
             */
            String customerNameText = customerField.getText().trim();
            String bookNameText = bookField.getText().trim();

            if (customerNameText.isEmpty() || bookNameText.isEmpty()) {
                showAlert("Input Error", "Please fill in all fields.");
                return;
            }

            if (!customerNameText.matches("[a-zA-Z0-9\\s]+") || !bookNameText.matches("[a-zA-Z0-9\\s]+")) {
                showAlert("Input Error", "Invalid input.");
                return;
            }

            try {
                Connection con = DBUtils.establishConnection();

                String customerQuery = "SELECT * FROM user WHERE username = ? AND role = 'user'";
                PreparedStatement customerStmt = con.prepareStatement(customerQuery);
                customerStmt.setString(1, customerNameText);
                ResultSet customerRs = customerStmt.executeQuery();

                if (!customerRs.next()) {
                    showInfo("Return Book", "Customer does not exist.");
                    customerStmt.close();
                    con.close();
                    return;
                }

                String bookQuery = "SELECT * FROM book WHERE book_name = ?";
                PreparedStatement bookStmt = con.prepareStatement(bookQuery);
                bookStmt.setString(1, bookNameText);
                ResultSet bookRs = bookStmt.executeQuery();

                if (!bookRs.next()) {
                    showInfo("Return Book", "Book does not exist.");
                    customerStmt.close();
                    bookStmt.close();
                    con.close();
                    return;
                }

                String borrowedQuery = "SELECT * FROM borrowed_book WHERE customer_name = ? AND book_name = ?";
                PreparedStatement borrowedStmt = con.prepareStatement(borrowedQuery);
                borrowedStmt.setString(1, customerNameText);
                borrowedStmt.setString(2, bookNameText);
                ResultSet borrowedRs = borrowedStmt.executeQuery();

                if (!borrowedRs.next()) {
                    showInfo("Return Book", "This customer did not borrow this book.");
                    customerStmt.close();
                    bookStmt.close();
                    borrowedStmt.close();
                    con.close();
                    return;
                }

                java.sql.Date issueDate = borrowedRs.getDate("date");
                java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
                long daysBetween = (today.getTime() - issueDate.getTime()) / (1000*60*60*24);
                int fineAmount = 0;

                if(daysBetween > 60){ // 2 months
                    fineAmount = (int)(daysBetween - 60) * 5;
                    String fineQuery = "INSERT INTO fines (customer_name, book_name, fine_amount) VALUES (?, ?, ?)";
                    PreparedStatement fineStmt = con.prepareStatement(fineQuery);
                    fineStmt.setString(1, customerNameText);
                    fineStmt.setString(2, bookNameText);
                    fineStmt.setInt(3, fineAmount);
                    fineStmt.executeUpdate();
                    fineStmt.close();
                }

                String deleteQuery = "DELETE FROM borrowed_book WHERE customer_name = ? AND book_name = ?";
                PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
                deleteStmt.setString(1, customerNameText);
                deleteStmt.setString(2, bookNameText);

                int rows = deleteStmt.executeUpdate();

                if(rows > 0){
                    showInfo("Return Book", "Book returned successfully. Fine: " + fineAmount);
                    customerField.clear();
                    bookField.clear();
                }

                customerStmt.close();
                bookStmt.close();
                borrowedStmt.close();
                deleteStmt.close();
                con.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        bookStatsBtn.setOnAction(e -> {
            System.out.println("Update Book");
        });

        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });
        backBtn.setOnAction(e -> {
            StaffView staffView = new StaffView(stage, currentUser);
            staffView.initializeComponents();
        });

        VBox StaffLayout = new VBox(15);
        StaffLayout.setPadding(new Insets(10));
        StaffLayout.getChildren().addAll(title,customerName,customerField,bookName, bookField, issueReturnLayout, buttonLayout );

        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}