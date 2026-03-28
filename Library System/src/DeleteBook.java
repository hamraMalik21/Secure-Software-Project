import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteBook {
    private User currentUser;
    private Stage stage;

    public DeleteBook(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Delete Book");
        title.setFont(new Font("Times New Roman",24));

        // book name
        Label bookNameLabel = new Label("Enter Name of Book you want to delete: ");
        bookNameLabel.setTextFill(Color.RED);
        TextField bookNameTx = new TextField();
        HBox bookNameH = new HBox(10, bookNameLabel, bookNameTx);




        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button delBtn = new Button("Delete Book");
        delBtn.setPrefWidth(200);

        HBox delBackH = new HBox(10, delBtn, backBtn);

        delBtn.setOnAction(e -> {
            String bookName = bookNameTx.getText().trim();
/*

 */
            // movie name empty or valid check
            if (bookName.isEmpty( )) {
                showAlert("Error", "Book Name field is empty");
                return;
            }else if(!InputValidator.validateBookName(bookName)){
                showAlert("Error", "Book Name Is Not Valid");
                return;
            }




            if (BookCheck.isBorrowed(bookName)) {
                showAlert("Error", "Book cannot be deleted, it is currently borrowed");
            }else if (BookCheck.bookExists(bookName)){

                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String sql = "DELETE FROM book WHERE book_name = ?";
                    stmt = con.prepareStatement(sql);
                    stmt.setString(1, bookName);

                    stmt.executeUpdate();

                    showSuccess("Success", "Book Removed successfully");

                    DBUtils.closeConnection(con, stmt);

                    // going to main screen after deleting
                    StaffView staffView = new StaffView(stage, currentUser);
                    staffView.initializeComponents();

                } catch (Exception ex) {
                    System.out.println("Insert error:" + ex.getMessage());
                    try {
                        DBUtils.closeConnection(con, stmt);
                    } catch (Exception ignored) {}
                }

            }
            else {
                showAlert("Error", "Book does not exist");
            }
        });


        backBtn.setOnAction(e -> {
            ManageBooksView manageBooksView = new ManageBooksView(stage,currentUser);
            manageBooksView.initializeComponents();
        });


        // Layout
        VBox addBookLayout = new VBox(15);
        addBookLayout.setPadding(new Insets(10));
        addBookLayout.getChildren().addAll(title,bookNameH,delBackH);

        // Scene then stage
        Scene scene = new Scene(addBookLayout, 730, 600);
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
