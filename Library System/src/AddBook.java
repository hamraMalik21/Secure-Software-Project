import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBook {
    private User currentUser;
    private Stage stage;

    public AddBook(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Add Book");
        title.setFont(new Font("Times New Roman",24));

        // book name
        Button bookNameBtn = new Button("Book Name");
        bookNameBtn.setPrefWidth(200);
        bookNameBtn.setDisable(true);
        TextField bookNameTx = new TextField();
        HBox bookNameH = new HBox(10, bookNameBtn, bookNameTx);

        // author name
        Button authorBtn = new Button("Author Name");
        authorBtn.setPrefWidth(200);
        authorBtn.setDisable(true);
        TextField authorTx = new TextField();
        HBox authorH = new HBox(10, authorBtn, authorTx);


        // book Description
        Button descriptionBtn = new Button("Book Description");
        descriptionBtn.setPrefWidth(200);
        descriptionBtn.setDisable(true);
        // trying TextArea for larger text field with Wrap functionality
        TextArea descriptionTx = new TextArea();
        descriptionTx.setPrefSize(500,100);
        descriptionTx.setWrapText(true);
        HBox descriptionH = new HBox(10, descriptionBtn, descriptionTx);




        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button addBtn = new Button("Add Book");
        addBtn.setPrefWidth(200);
        HBox addBackH = new HBox(10, addBtn, backBtn);

        addBtn.setOnAction(e -> {
            String bookName = bookNameTx.getText().trim();
            String bookAuthor = authorTx.getText().trim();
            String bookDescription = descriptionTx.getText().trim();
/*
            if (movieName.isEmpty( ) ||movieDirector.isEmpty() || movieDuration.isEmpty()||movieDescription.isEmpty()||movieGenre.isEmpty()) {
                showAlert("Error", "at least one field is empty");
                return;
            }

 */
            // movie name empty or valid check
            if (bookName.isEmpty( )) {
                showAlert("Error", "Book field is empty");
                return;
            }else if(!InputValidator.validateBookName(bookName)){
                showAlert("Error", "Book Name Is Not Valid");
                return;
            }

            // movie director empty or valid check
            if (bookAuthor.isEmpty( )) {
                showAlert("Error", "Author Name field is empty");
                return;
            }else if(!InputValidator.validateAuthorName(bookAuthor)){
                showAlert("Error", "Author Name Is Not Valid");
                return;
            }

            // movie duration empty or valid check
            if (bookDescription.isEmpty( )) {
                showAlert("Error", "Book Description field is empty");
                return;
            }else if(!InputValidator.validateBookDescription(bookDescription)){
                showAlert("Error", "Duration Is Not Valid");
                return;
            }



            if (BookCheck.bookExists(bookName)) {
                showAlert("Error", "Book cannot be added, already exist");
            } else {
                Connection con = null;
                PreparedStatement stmt = null;

                try {
                    con = DBUtils.establishConnection();

                    String sql = "INSERT INTO book (book_name, author_name, book_description) VALUES (?, ?, ?)";
                    stmt = con.prepareStatement(sql);
                    stmt.setString(1, bookName);
                    stmt.setString(2, bookAuthor);
                    stmt.setString(3, bookDescription);

                    stmt.executeUpdate();

                    showSuccess("Success", "Book Added successfully");

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
            }
        });


        backBtn.setOnAction(e -> {
            ManageBooksView manageBooksView = new ManageBooksView(stage,currentUser);
            manageBooksView.initializeComponents();
        });


        // Layout
        VBox addBookLayout = new VBox(15);
        addBookLayout.setPadding(new Insets(10));
        addBookLayout.getChildren().addAll(title,bookNameH,authorH,descriptionH,addBackH);

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
