package controller;

import model.Book;
import model.Loan;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanController {
    private final List<Loan> loans = new ArrayList<>();

    public List<Loan> getLoans() {
        return loans;
    }

    public void loanBook(Book book, User user) {
        if (book != null && user != null && !book.isLoaned()) {
            book.setLoaned(true);
            loans.add(new Loan(loans.size() + 1, book.getId(), user.getId(), LocalDate.now(), null));
        }
    }

    public void returnBook(Book book) {
        Loan loan = loans.stream().filter(l -> l.getBookId() == book.getId() && l.getReturnDate() == null).findFirst().orElse(null);
        if (loan != null) {
            loan.setReturnDate(LocalDate.now());
            book.setLoaned(false);
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
}
