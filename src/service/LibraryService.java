package service;

import dao.BookDAO;
import dao.MemberDAO;
import dao.TransactionDAO;
import model.Book;
import model.Member;
import model.Transaction;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService {

    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private TransactionDAO transactionDAO;

    // Borrowing limits
    private static final int STUDENT_MAX_BOOKS = 3;
    private static final int FACULTY_MAX_BOOKS = 5;
    private static final int STUDENT_BORROW_DAYS = 14;
    private static final int FACULTY_BORROW_DAYS = 30;
    private static final double FINE_PER_DAY = 2.0;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // BORROW A BOOK
    public String borrowBook(int bookId, int memberId) {
        Book book = bookDAO.getBookById(bookId);
        Member member = memberDAO.getMemberById(memberId);

        // Validation 1: Does book exist?
        if (book == null) {
            return "Error: Book not found.";
        }

        // Validation 2: Does member exist?
        if (member == null) {
            return "Error: Member not found.";
        }

        // Validation 3: Is book available?
        if (book.getAvailableQuantity() <= 0) {
            return "Error: Book '" + book.getTitle() + "' is not available.";
        }

        // Validation 4: Has member reached borrowing limit?
        int maxBooks = member.getMemberType().equals("FACULTY") ? FACULTY_MAX_BOOKS : STUDENT_MAX_BOOKS;
        int currentBorrows = transactionDAO.countActiveBorrows(memberId);

        if (currentBorrows >= maxBooks) {
            return "Error: " + member.getName() + " has reached the borrowing limit (" + maxBooks + " books).";
        }

        // Calculate due date based on member type
        int borrowDays = member.getMemberType().equals("FACULTY") ? FACULTY_BORROW_DAYS : STUDENT_BORROW_DAYS;
        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(borrowDays);

        // Create transaction
        Transaction transaction = new Transaction(
                bookId, memberId, today.toString(), due.toString());

        // All validations passed — execute borrow
        boolean transactionCreated = transactionDAO.createTransaction(transaction);
        boolean quantityUpdated = bookDAO.updateAvailableQuantity(bookId, -1); // -1 = decrease

        if (transactionCreated && quantityUpdated) {
            return "Success! '" + book.getTitle() + "' borrowed by " + member.getName() +
                    ". Due date: " + due.toString() + " (" + borrowDays + " days)";
        } else {
            return "Error: Borrow failed due to system error.";
        }
    }

    // RETURN A BOOK
    public String returnBook(int bookId, int memberId) {
        // Find the active transaction
        Transaction transaction = transactionDAO.getActiveTransaction(bookId, memberId);

        if (transaction == null) {
            return "Error: No active borrow record found for this book and member.";
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.parse(transaction.getDueDate());
        double fine = 0.0;

        // Calculate fine if overdue
        if (today.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
            fine = daysOverdue * FINE_PER_DAY;
        }

        // Update transaction and book availability
        boolean transactionUpdated = transactionDAO.returnBook(
                transaction.getTransactionId(), today.toString(), fine);
        boolean quantityUpdated = bookDAO.updateAvailableQuantity(bookId, 1); // +1 = increase

        Book book = bookDAO.getBookById(bookId);

        if (transactionUpdated && quantityUpdated) {
            String result = "Success! '" + book.getTitle() + "' returned.";
            if (fine > 0) {
                result += " Fine: ₹" + fine + " (" +
                        ChronoUnit.DAYS.between(dueDate, today) + " days overdue)";
            }
            return result;
        } else {
            return "Error: Return failed due to system error.";
        }
    }
}