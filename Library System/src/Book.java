public class Book {
    private int book_id;
    private String book_name;
    private String author_name;
    private String book_description;


    //this is for select *
    public Book(int book_id, String book_name, String author_name, String book_description) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.book_description = book_description;
    }

    //this is for adding a new book
    public Book(String book_name, String author_name, String book_description) {
        this.book_name = book_name;
        this.author_name = author_name;
        this.book_description = book_description;
    }

    public int getBook_id() {
        return book_id;
    }


    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getBook_description() {
        return book_description;
    }

    public void setBook_description(String book_description) {
        this.book_description = book_description;
    }
}
