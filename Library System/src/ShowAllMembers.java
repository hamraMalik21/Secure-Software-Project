import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
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

public class ShowAllMembers {
    private User currentUser;
    private Stage stage;

    public ShowAllMembers(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public ShowAllMembers(Stage stage) {
        this.stage = stage;
    }

    public void initializeComponents() {
        stage.setTitle("Show All Members");

        // To display data in a table, use the JavaFX TableView
        TableView<User> table = new TableView<>();

        // Define the first column of the table, <Books, Integer> means the data type
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        // PropertyValueFactory<>("id") will call the getId() method in the model class
        // which will fill the cell with the command id value for every row.
        idColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // User name
        TableColumn<User, String> memberNameColumn = new TableColumn<>("Member Name");
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));







        // Add all columns to the table
        table.getColumns().addAll(idColumn, memberNameColumn);

        // We will use Observable List to hold the data retrieved from the database
        ObservableList<User> MemberList = FXCollections.observableArrayList();
        
        // Retrieve data from DB and fill up the table
        try{
            Connection con = DBUtils.establishConnection();
            String query = "SELECT user_id, username FROM user WHERE role = 'member'";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            
            while (rs.next()) {
                User user = new User(rs.getInt("user_id"), rs.getString("username"));
                MemberList.add(user);
            }

            DBUtils.closeConnection(con, stmt);
        }catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
        //Set the table to watch the observable list
        //the table will read data from it, and will also update upon any change
        table.setItems(MemberList);




        // back button based on role, trying new thing

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);

        backBtn.setOnAction(e -> {
            ManageMembersView manageMembersView = new ManageMembersView(stage, currentUser);
            manageMembersView.initializeComponents();
        });



        HBox logoutBackBtns = new HBox(backBtn);


        // Create the layout (VBox that contains the table)
        VBox vbox = new VBox(table,logoutBackBtns);
        // Add the layout to the scene
        Scene scene = new Scene(vbox, 800, 500);

        //Add the scene to stage
        stage.setScene(scene);
        stage.show();
        return;
    }

}