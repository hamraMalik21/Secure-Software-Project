import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ManageBooksView {
    private User currentUser;
    private Stage stage;

    public ManageBooksView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void initializeComponents() {
        // Title
        Label title = new Label("Manage Books");
        title.setFont(new Font("Times New Roman",24));


        Button addBookBtn = new Button("Add Book");
        addBookBtn.setPrefWidth(200);

        Button deleteBookBtn = new Button("Delete Book");
        deleteBookBtn.setPrefWidth(200);

        Button updateBookBtn = new Button("Update Book");
        updateBookBtn.setPrefWidth(200);

        Button showBookBtn = new Button("Show All Books");
        showBookBtn.setPrefWidth(200);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(95);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(95);

        HBox buttonLayout = new HBox(logoutBtn, backBtn);
        buttonLayout.setSpacing(10);

        // Actions *****
        addBookBtn.setOnAction(e -> {
            AddBook addBook = new AddBook(stage, currentUser);
            addBook.show();
            
        });

        deleteBookBtn.setOnAction(e -> {
            DeleteBook deleteBook = new DeleteBook(stage, currentUser);
            deleteBook.show();


        });

        updateBookBtn.setOnAction(e -> {
            UpdateBook updateBook = new UpdateBook(stage, currentUser);
            updateBook.show();
        });

        showBookBtn.setOnAction(e -> {
            ShowAllBooks showAllBooks = new ShowAllBooks(stage, currentUser);
            showAllBooks.initializeComponents();
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
        StaffLayout.getChildren().addAll(title,addBookBtn,deleteBookBtn,updateBookBtn,showBookBtn, buttonLayout);


        // Scene then stage
        Scene scene = new Scene(StaffLayout, 600, 600);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

}
