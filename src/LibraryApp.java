import dao.BookDAO;
import dao.MemberDAO;
import dao.TransactionDAO;
import model.Book;
import model.Member;
import model.Transaction;
import service.LibraryService;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private static Scanner scanner = new Scanner(System.in);
    private static LibraryService libraryService = new LibraryService();
    private static BookDAO bookDAO = new BookDAO();
    private static MemberDAO memberDAO = new MemberDAO();
    private static TransactionDAO transactionDAO = new TransactionDAO();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1: addBook(); break;
                case 2: viewAllBooks(); break;
                case 3: searchBook(); break;
                case 4: addMember(); break;
                case 5: viewAllMembers(); break;
                case 6: borrowBook(); break;
                case 7: returnBook(); break;
                case 8: viewMemberTransactions(); break;
                case 9:
                    System.out.println("Thank you for using Library Management System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   LIBRARY MANAGEMENT SYSTEM         ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Add New Book                    ║");
        System.out.println("║  2. View All Books                  ║");
        System.out.println("║  3. Search Book by Title            ║");
        System.out.println("║  4. Add New Member                  ║");
        System.out.println("║  5. View All Members                ║");
        System.out.println("║  6. Borrow Book                     ║");
        System.out.println("║  7. Return Book                     ║");
        System.out.println("║  8. View Member Transactions        ║");
        System.out.println("║  9. Exit                            ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // 1. ADD BOOK
    private static void addBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        int quantity = getIntInput("Quantity: ");

        Book book = new Book(0, isbn, title, author, genre, quantity, quantity);
        if (bookDAO.addBook(book)) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Failed to add book.");
        }
    }

    // 2. VIEW ALL BOOKS
    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in library.");
        } else {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    // 3. SEARCH BOOK
    private static void searchBook() {
        System.out.println("\n--- Search Book ---");
        System.out.print("Enter title keyword: ");
        String keyword = scanner.nextLine();
        List<Book> books = bookDAO.searchByTitle(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    // 4. ADD MEMBER
    private static void addMember() {
        System.out.println("\n--- Add New Member ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Membership Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Type (STUDENT/FACULTY): ");
        String type = scanner.nextLine().toUpperCase();

        Member member = new Member(0, name, email, phone, date, type);
        if (memberDAO.addMember(member)) {
            System.out.println("Member added successfully!");
        } else {
            System.out.println("Failed to add member.");
        }
    }

    // 5. VIEW ALL MEMBERS
    private static void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        List<Member> members = memberDAO.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered.");
        } else {
            for (Member m : members) {
                System.out.println(m);
            }
        }
    }

    // 6. BORROW BOOK
    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        int bookId = getIntInput("Enter Book ID: ");
        int memberId = getIntInput("Enter Member ID: ");
        String result = libraryService.borrowBook(bookId, memberId);
        System.out.println(result);
    }

    // 7. RETURN BOOK
    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        int bookId = getIntInput("Enter Book ID: ");
        int memberId = getIntInput("Enter Member ID: ");
        String result = libraryService.returnBook(bookId, memberId);
        System.out.println(result);
    }

    // 8. VIEW MEMBER TRANSACTIONS
    private static void viewMemberTransactions() {
        System.out.println("\n--- Member Transactions ---");
        int memberId = getIntInput("Enter Member ID: ");
        List<Transaction> transactions = transactionDAO.getTransactionsByMember(memberId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this member.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }

    // Helper method to get integer input safely
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            System.out.print(prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume the newline
        return value;
    }
}
