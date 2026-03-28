import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateBook {
    private Stage stage;
    private User currentUser;

    private String currentBookName;
    private String currentBookAuthor;
    private String currentBookDescription;

    // Updated constructor to accept a Book object
    public UpdateBook(Stage stage, User currentUser, Book book) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.currentBookName = book.getBook_name();
        this.currentBookAuthor = book.getAuthor_name();
        this.currentBookDescription = book.getBook_description();
    }

    public void show() {

        Label title = new Label("Update Book Details");
        title.setFont(new Font("Times New Roman",24));

        Button bookNameBtn = new Button("Book Name");
        bookNameBtn.setPrefWidth(200);
        bookNameBtn.setDisable(true);
        TextField bookNameTx = new TextField(currentBookName);
        HBox bookNameH = new HBox(10, bookNameBtn, bookNameTx);

        Button authorBtn = new Button("Author Name");
        authorBtn.setPrefWidth(200);
        authorBtn.setDisable(true);
        TextField authorTx = new TextField(currentBookAuthor);
        HBox authorH = new HBox(10, authorBtn, authorTx);

        Button descriptionBtn = new Button("Book Description");
        descriptionBtn.setPrefWidth(200);
        descriptionBtn.setDisable(true);
        TextArea descriptionTx = new TextArea(currentBookDescription);
        descriptionTx.setPrefSize(500,100);
        descriptionTx.setWrapText(true);
        HBox descriptionH = new HBox(10, descriptionBtn, descriptionTx);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button updateBtn = new Button("Update Book");
        updateBtn.setPrefWidth(200);
        HBox updateBackH = new HBox(10, updateBtn, backBtn);

        updateBtn.setOnAction(e -> {
            String bookAuthor = authorTx.getText().trim();
            String bookDescription = descriptionTx.getText().trim();

            if (bookAuthor.isEmpty()) {
                showAlert("Error", "Book Author field is empty");
                return;
            } else if(!InputValidator.validateAuthorName(bookAuthor)){
                showAlert("Error", "Author Name Is Not Valid");
                return;
            }

            if (bookDescription.isEmpty()) {
                showAlert("Error", "Book Description field is empty");
                return;
            } else if(!InputValidator.validateBookDescription(bookDescription)){
                showAlert("Error", "Book description Is Not Valid");
                return;
            } else if (bookDescription.length()< 10){
                showAlert("Error", "Book description Is too Short");
                return;
            }

            try {
                Connection con = DBUtils.establishConnection();
                String sql = "UPDATE book SET author_name = ?, book_description = ? WHERE book_name = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, bookAuthor);
                stmt.setString(2, bookDescription);
                stmt.setString(3, currentBookName);
                stmt.executeUpdate();

                showSuccess("Success", "Book updated successfully");
                DBUtils.closeConnection(con, stmt);

                StaffView staffView = new StaffView(stage, currentUser);
                staffView.initializeComponents();

            } catch (Exception ex) {
                System.out.println("Insert error:" + ex.getMessage());
            }
        });

        backBtn.setOnAction(e -> {
            ManageBooksView manageBooksView = new ManageBooksView(stage,currentUser);
            manageBooksView.initializeComponents();
        });

        VBox updateBookLayout = new VBox(15);
        updateBookLayout.setPadding(new Insets(10));
        updateBookLayout.getChildren().addAll(title,bookNameH,authorH, descriptionH,updateBackH);

        Scene scene = new Scene(updateBookLayout, 730, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}