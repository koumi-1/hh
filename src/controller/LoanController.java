package controller;

import lombok.AllArgsConstructor;
import model.Book;
import model.Loan;
import model.User;
import utils.CSVUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanController {
    private static final String LOANS_FILE = "resources/loans.csv";
    private final List<Loan> loans = new ArrayList<>();
    private final BookController bookController;
    private final UserController userController;

    public LoanController(BookController bookController, UserController userController) {
        this.bookController = bookController;
        this.userController = userController;
        loadLoansFromFile();
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void loanBook(int bookId, int userId) {
        User user = userController.getUserById(userId);
        Book book = bookController.getBookById(bookId);
        if (book != null && user != null && !book.isLoaned()) {
            book.setLoaned(true);
            Loan newLoan = new Loan(loans.size() + 1, book.getId(), user.getId(), LocalDate.now(), null);
            loans.add(newLoan);
            saveLoansToFile();
        }
    }

    public void returnBook(Book book) {
        Loan loan = loans.stream()
                .filter(l -> l.getBookId() == book.getId() && l.getReturnDate() == null)
                .findFirst()
                .orElse(null);
        if (loan != null) {
            loan.setReturnDate(LocalDate.now());
            book.setLoaned(false);
            saveLoansToFile();
        }
    }

    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> userLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUserId() == userId) {
                userLoans.add(loan);
            }
        }
        return userLoans;
    }

    private void loadLoansFromFile() {
        List<String[]> rows = CSVUtils.readFromCSV(LOANS_FILE);
        for (String[] row : rows) {
            if (row.length == 5) {
                Loan loan = new Loan(
                        Integer.parseInt(row[0]),  // Loan ID
                        Integer.parseInt(row[1]),  // Book ID
                        Integer.parseInt(row[2]),  // User ID
                        LocalDate.parse(row[3]),   // Loan Date
                        row[4].isEmpty() ? null : LocalDate.parse(row[4])  // Return Date
                );
                loans.add(loan);
            }
        }
    }

    private void saveLoansToFile() {
        List<String[]> rows = new ArrayList<>();
        for (Loan loan : loans) {
            rows.add(new String[]{
                    String.valueOf(loan.getLoanId()),
                    String.valueOf(loan.getBookId()),
                    String.valueOf(loan.getUserId()),
                    loan.getLoanDate().toString(),
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : ""
            });
        }
        CSVUtils.writeToCSV(LOANS_FILE, rows);
    }
}
