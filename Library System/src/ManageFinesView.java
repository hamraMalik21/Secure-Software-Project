import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ManageFinesView {
    private User currentUser;
    private Stage stage;

    public ManageFinesView(Stage primaryStage, User user) {
        this.stage = primaryStage;
        this.currentUser = user;
    }

    public void initializeComponents() {
        stage.setTitle("Manage Fines");

        TableView<FineRecord> table = new TableView<>();

        TableColumn<FineRecord, String> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));

        TableColumn<FineRecord, String> bookColumn = new TableColumn<>("Book");
        bookColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookName()));

        TableColumn<FineRecord, String> dateColumn = new TableColumn<>("Borrowed Date");
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrowedDate()));

        TableColumn<FineRecord, Integer> daysColumn = new TableColumn<>("Late Days");
        daysColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getLateDays()).asObject());

        TableColumn<FineRecord, Integer> fineColumn = new TableColumn<>("Fine");
        fineColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFineAmount()).asObject());

        table.getColumns().addAll(customerColumn, bookColumn, dateColumn, daysColumn, fineColumn);

        ObservableList<FineRecord> fineList = FXCollections.observableArrayList();

        try {
            Connection con = DBUtils.establishConnection();
            String query = "SELECT customer_name, book_name, date FROM borrowed_book";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                String bookName = rs.getString("book_name");
                LocalDate borrowedDate = rs.getDate("date").toLocalDate();
                LocalDate today = LocalDate.now();

                long totalDays = ChronoUnit.DAYS.between(borrowedDate, today);
                int lateDays = 0;
                int fine = 0;

                if (totalDays > 7) {
                    lateDays = (int) (totalDays - 7);
                    fine = lateDays * 5;
                }

                fineList.add(new FineRecord(customerName, bookName, borrowedDate.toString(), lateDays, fine));
            }

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        table.setItems(fineList);

        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(120);
        backBtn.setOnAction(e -> {
            StaffView staffView = new StaffView(stage, currentUser);
            staffView.initializeComponents();
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(120);
        logoutBtn.setOnAction(e -> {
            UserLogin loginView = new UserLogin(stage);
            loginView.initializeComponents();
        });

        HBox buttons = new HBox(10, backBtn, logoutBtn);
        VBox layout = new VBox(10, table, buttons);

        Scene scene = new Scene(layout, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static class FineRecord {
        private String customerName;
        private String bookName;
        private String borrowedDate;
        private int lateDays;
        private int fineAmount;

        public FineRecord(String customerName, String bookName, String borrowedDate, int lateDays, int fineAmount) {
            this.customerName = customerName;
            this.bookName = bookName;
            this.borrowedDate = borrowedDate;
            this.lateDays = lateDays;
            this.fineAmount = fineAmount;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getBookName() {
            return bookName;
        }

        public String getBorrowedDate() {
            return borrowedDate;
        }

        public int getLateDays() {
            return lateDays;
        }

        public int getFineAmount() {
            return fineAmount;
        }
    }
}