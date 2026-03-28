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
    private Stage   stage;
    private User    currentUser;

    private String  currentBookName;
    private String  currentBookAuthor;
    private String  currentBookDescription;


    public UpdateBook(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }


    public void show() {

        // Title
        Label title = new Label("Update Book Details");
        title.setFont(new Font("Times New Roman",24));

        // book name
        Button bookNameBtn = new Button("Book Name");
        bookNameBtn.setPrefWidth(200);
        bookNameBtn.setDisable(true);
        TextField bookNameTx = new TextField(currentBookName);
        HBox bookNameH = new HBox(10, bookNameBtn, bookNameTx);

        // author name
        Button authorBtn = new Button("Author Name");
        authorBtn.setPrefWidth(200);
        authorBtn.setDisable(true);
        TextField authorTx = new TextField(currentBookAuthor);
        HBox authorH = new HBox(10, authorBtn, authorTx);


        // movie Description
        Button descriptionBtn = new Button("Book Description");
        descriptionBtn.setPrefWidth(200);
        descriptionBtn.setDisable(true);
        // trying TextArea for larger text field with Wrap functionality
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
            String bookName = bookNameTx.getText().trim();
            String bookAuthor = authorTx.getText().trim();
            String bookDescription = descriptionTx.getText().trim();
            // book name empty or valid check
            if (bookName.isEmpty( )) {
                showAlert("Error", "Book Name is empty");
                return;
            }else if(!InputValidator.validateBookName(bookName)){
                showAlert("Error", "Book Name Is Not Valid");
                return;
            }

            // author empty or valid check
            if (bookAuthor.isEmpty( )) {
                showAlert("Error", "Book Author field is empty");
                return;
            }else if(!InputValidator.validateAuthorName(bookAuthor)){
                showAlert("Error", "Author Name Is Not Valid");
                return;
            }


            // movie description empty or valid or length check
            if (bookDescription.isEmpty( )) {
                showAlert("Error", "Book Description field is empty");
                return;
            }else if(!InputValidator.validateBookDescription(bookDescription)){
                showAlert("Error", "Book description Is Not Valid");
                return;
            }else if (bookDescription.length()< 10){
                showAlert("Error", "Book description Is too Short");
                return;
            }


                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String sql = "UPDATE book SET author_name = ?, book_description = ? WHERE book_name = ?";
                    stmt = con.prepareStatement(sql);
                    stmt.setString(1, bookAuthor);
                    stmt.setString(2, bookDescription);
                    stmt.setString(3, currentBookName); 


                    stmt.executeUpdate();

                    showSuccess("Success", "Book updated successfully");

                    DBUtils.closeConnection(con, stmt);

                    // going to main screen after adding
                    StaffView staffView = new StaffView(stage, currentUser);
                    staffView.initializeComponents();

                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {}
                }

        });


        backBtn.setOnAction(e -> {
            ManageBooksView manageBooksView = new ManageBooksView(stage,currentUser);
            manageBooksView.initializeComponents();
        });


        // Layout
        VBox updateBookLayout = new VBox(15);
        updateBookLayout.setPadding(new Insets(10));
        updateBookLayout.getChildren().addAll(title,bookNameH,authorH, descriptionH,updateBackH);

        // Scene then stage
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
        // learned of a new alert type ^^
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
