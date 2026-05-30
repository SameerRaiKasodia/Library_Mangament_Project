package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Transaction;
import util.DatabaseConnection;

public class TransactionDAO {

    // CREATE A NEW TRANSACTION (when borrowing)
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (book_id, member_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getMemberId());
            stmt.setString(3, transaction.getBorrowDate());
            stmt.setString(4, transaction.getDueDate());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error creating transaction: " + e.getMessage());
            return false;
        }
    }

    // GET ACTIVE TRANSACTION (not yet returned) for a book-member pair
    public Transaction getActiveTransaction(int bookId, int memberId) {
        String sql = "SELECT * FROM transactions WHERE book_id = ? AND member_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"),
                        rs.getDouble("fine")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transaction: " + e.getMessage());
        }
        return null;
    }

    // UPDATE TRANSACTION ON RETURN
    public boolean returnBook(int transactionId, String returnDate, double fine) {
        String sql = "UPDATE transactions SET return_date = ?, fine = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, returnDate);
            stmt.setDouble(2, fine);
            stmt.setInt(3, transactionId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
            return false;
        }
    }

    // GET ALL TRANSACTIONS FOR A MEMBER
    public List<Transaction> getTransactionsByMember(int memberId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"),
                        rs.getDouble("fine")
                );
                transactions.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }

    // COUNT ACTIVE BORROWED BOOKS FOR A MEMBER
    public int countActiveBorrows(int memberId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE member_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting borrows: " + e.getMessage());
        }
        return 0;
    }
}