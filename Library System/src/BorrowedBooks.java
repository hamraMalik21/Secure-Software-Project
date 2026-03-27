public class BorrowedBooks {

    private int id;
    private String customer_name;
    private String book_name;
    private String date;

    public BorrowedBooks(int id, String customer_name, String book_name, String date) {
        this.id = id;
        this.customer_name = customer_name;
        this.book_name = book_name;
        this.date = date;
    }

    public BorrowedBooks(String customer_name, String book_name, String date) {
        this.customer_name = customer_name;
        this.book_name = book_name;
        this.date = date;
    }

    public int getId() {
        return id;
    }


    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
