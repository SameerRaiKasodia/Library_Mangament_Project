package model;

public class Transaction {

    private int transactionId;
    private int bookId;
    private int memberId;
    private String borrowDate;    // Using String for simplicity (can use LocalDate later)
    private String dueDate;
    private String returnDate;    // null if not returned yet
    private double fine;

    // Default constructor
    public Transaction() {
    }

    // Constructor for new borrow
    public Transaction(int bookId, int memberId, String borrowDate, String dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
    }

    // Full constructor
    public Transaction(int transactionId, int bookId, int memberId,
                       String borrowDate, String dueDate, String returnDate, double fine) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    @Override
    public String toString() {
        return "Transaction [ID=" + transactionId + ", BookID=" + bookId +
                ", MemberID=" + memberId + ", Borrow=" + borrowDate +
                ", Due=" + dueDate + ", Return=" +
                (returnDate != null ? returnDate : "Not Returned") +
                ", Fine=₹" + fine + "]";
    }
}