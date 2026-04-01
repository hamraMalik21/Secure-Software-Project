import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteMember {
    private User currentUser;
    private Stage stage;

    public DeleteMember(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }
    public void show() {
        // Title
        Label title = new Label("Delete Member");
        title.setFont(new Font("Times New Roman",24));

        // book name
        Label memberNameLabel = new Label("Enter Name of Member you want to delete: ");
        memberNameLabel.setTextFill(Color.RED);
        TextField memberNameTx = new TextField();
        HBox bookNameH = new HBox(10, memberNameLabel, memberNameTx);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(200);
        Button delBtn = new Button("Delete Member");
        delBtn.setPrefWidth(200);

        HBox delBackH = new HBox(10, delBtn, backBtn);

        delBtn.setOnAction(e -> {
                    String memberName = memberNameTx.getText().trim();

                    // member name empty or valid check
                    if (memberName.isEmpty()) {
                        showAlert("Error", "Member Name field is empty");
                        return;
                    } else if (!InputValidator.validateMemberName(memberName)) {
                        showAlert("Error", "Member Name Is Not Valid");
                        return;
                    }

                    /// /
                    Connection con = null;
                    PreparedStatement stmt = null;

                    try {
                        con = DBUtils.establishConnection();

                        String sql = "SELECT * FROM borrowed_book WHERE username = ? ";
                        stmt = con.prepareStatement(sql);
                        stmt.setString(1, memberName);

                        stmt.executeUpdate();

                        if (sql != null) {
                            showAlert("Error", "Cannot remove member they have borrowed a book");
                        } else {


                            try {
                                con = DBUtils.establishConnection();

                                String sql2 = "DELETE FROM user WHERE username = ? AND role = 'member'";
                                stmt = con.prepareStatement(sql2);
                                stmt.setString(1, memberName);

                                stmt.executeUpdate();

                                showSuccess("Success", "Member Removed successfully");

                                DBUtils.closeConnection(con, stmt);

                                // going to main screen after deleting
                                StaffView staffView = new StaffView(stage, currentUser);
                                staffView.initializeComponents();

                            } catch (Exception ex) {
                                System.out.println("Insert error:" + ex.getMessage());
                                try {
                                    DBUtils.closeConnection(con, stmt);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Insert error:" + ex.getMessage());
                    }

                //i want to add the below logic inside an if statement, the if statement will first check if the member has a book borrowed or not, if he doesnt
                //then the delete will happen, if he has a book then in the else block i'll mention something like Cannot remove member they have borrowed a book




                backBtn.setOnAction(event -> {
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
    });

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