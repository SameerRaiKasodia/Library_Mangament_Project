package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import util.DatabaseConnection;

public class BookDAO {

    // 1. ADD A NEW BOOK
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, genre, quantity, available_quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getGenre());
            stmt.setInt(5, book.getQuantity());
            stmt.setInt(6, book.getAvailableQuantity());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    // 2. GET ALL BOOKS
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("quantity"),
                        rs.getInt("available_quantity")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
        return books;
    }

    // 3. SEARCH BOOK BY TITLE
    public List<Book> searchByTitle(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("quantity"),
                        rs.getInt("available_quantity")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
        return books;
    }

    // 4. UPDATE AVAILABLE QUANTITY (for borrow/return)
    public boolean updateAvailableQuantity(int bookId, int change) {
        String sql = "UPDATE books SET available_quantity = available_quantity + ? WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, change);   // negative for borrow, positive for return
            stmt.setInt(2, bookId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Error updating quantity: " + e.getMessage());
            return false;
        }
    }

    // 5. GET BOOK BY ID
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("quantity"),
                        rs.getInt("available_quantity")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching book: " + e.getMessage());
        }
        return null;
    }
}