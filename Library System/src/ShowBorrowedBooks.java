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

public class ShowBorrowedBooks {
    private User currentUser;
    private Stage stage;

    public ShowBorrowedBooks(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        stage.setTitle("Borrowed Books");

        // To display data in a table, use the JavaFX TableView
        TableView<BorrowedBooks> table = new TableView<>();

        // Define the first column of the table, <Books, Integer> means the data type
        TableColumn<BorrowedBooks, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Book name
        TableColumn<BorrowedBooks, String> bookNameColumn = new TableColumn<>("Book Name");
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("book_name"));

        // Author Name
        TableColumn<BorrowedBooks, String> customer_name = new TableColumn<>("Customer Name");
        customer_name.setCellValueFactory(new PropertyValueFactory<>("customer_name"));

        // Book Description
        TableColumn<BorrowedBooks, String> date = new TableColumn<>("Book Borrowed");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));



        // Add all columns to the table
        table.getColumns().addAll(idColumn, bookNameColumn, customer_name, date);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<BorrowedBooks> BorrowedBookList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            if (AuthorizationService.isUser(currentUser)){
                String query = "SELECT id, customer_name, book_name, date FROM borrowed_book WHERE customer_name = ?";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,  currentUser.getUsername());
                System.out.println(currentUser.getUsername());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    BorrowedBooks borrowedBook = new BorrowedBooks(rs.getInt("id"), rs.getString("customer_name"), rs.getString("book_name")
                            ,  rs.getString("date"));
                    BorrowedBookList.add(borrowedBook);
                }
                DBUtils.closeConnection(con, stmt);

            }else{
                String query = "SELECT id, book_name, customer_name, date FROM borrowed_book";

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    BorrowedBooks borrowedBook = new BorrowedBooks(rs.getInt("id"), rs.getString("book_name"), rs.getString("customer_name")
                            ,  rs.getString("date"));
                    BorrowedBookList.add(borrowedBook);
                }
                DBUtils.closeConnection(con, stmt);

            }

        }catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
        //Set the table to watch the observable list
        //the table will read data from it, and will also update upon any change
        table.setItems(BorrowedBookList);

        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            if (AuthorizationService.isStaff(currentUser)){
                StaffView staffView = new StaffView(stage,currentUser);
                staffView.initializeComponents();
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