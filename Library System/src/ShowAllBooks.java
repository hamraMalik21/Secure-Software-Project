import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowAllBooks {
    private User currentUser;
    private Stage stage;

    public ShowAllBooks(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        stage.setTitle("Show All Books");

        // To display data in a table, use the JavaFX TableView
        TableView<Book> table = new TableView<>();

        // Define the first column of the table, <Books, Integer> means the data type
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));

        // Book name
        TableColumn<Book, String> bookNameColumn = new TableColumn<>("Book Name");
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("book_name"));

        // Author Name
        TableColumn<Book, String> author_name = new TableColumn<>("Author");
        author_name.setCellValueFactory(new PropertyValueFactory<>("author_name"));

        // Book Description
        TableColumn<Book, String> bookDescription = new TableColumn<>("Book Description");
        bookDescription.setCellValueFactory(new PropertyValueFactory<>("book_description"));



        // Add all columns to the table
        table.getColumns().addAll(idColumn, bookNameColumn, author_name, bookDescription);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<Book> BookList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT book_id, book_name, author_name, book_description FROM book";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            
            while (rs.next()) {
                Book book = new Book(rs.getInt("book_id"), rs.getString("book_name"), rs.getString("author_name")
                        ,  rs.getString("book_description"));
                BookList.add(book);
            }

            DBUtils.closeConnection(con, stmt);
        }catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
        //Set the table to watch the observable list
        //the table will read data from it, and will also update upon any change
        table.setItems(BookList);

        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            if (AuthorizationService.isStaff(currentUser)){
                ManageBooksView manageBooksView = new ManageBooksView(stage,currentUser);
                manageBooksView.initializeComponents();
            }else if (AuthorizationService.isUser(currentUser)){
                CustomerView customerView = new CustomerView(stage,currentUser);
                customerView.initializeComponents();
            }
        });

        // to logout
        Button logoutBtn = new Button("logout");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            UserLogin userLogin = new UserLogin(stage);
            userLogin.initializeComponents();
        });

        HBox logoutBackBtns = new HBox(backBtn,logoutBtn);


        // Create the layout (VBox that contains the table)
        VBox vbox = new VBox(table,logoutBackBtns);
        // Add the layout to the scene
        Scene scene = new Scene(vbox, 1100, 500);

        //Add the scene to stage
        stage.setScene(scene);
        stage.show();
        return;
    }

}