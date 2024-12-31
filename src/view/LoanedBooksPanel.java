package view;

import controller.BookController;
import controller.LoanController;
import model.Book;
import model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanedBooksPanel extends JPanel {
    private final DefaultTableModel loanTableModel;
    private final JTable loanTable;
    private final LoanController loanController;
    private final BookController bookController;

    public LoanedBooksPanel(LoanController loanController, BookController bookController) {
        this.loanController = loanController;
        this.bookController = bookController;

        setLayout(new BorderLayout());

        String[] columns = {"Loan ID", "Book ID", "Book Title", "User ID", "Loan Date", "Return Date"};
        loanTableModel = new DefaultTableModel(columns, 0);
        loanTable = new JTable(loanTableModel);
        loanTable.setRowHeight(25);
        refreshLoanTable();

        add(new JScrollPane(loanTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton returnBookButton = new JButton("Return Book");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(returnBookButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        returnBookButton.addActionListener(e -> handleReturnBook());
        refreshButton.addActionListener(e -> refreshLoanTable());
    }

    private void handleReturnBook() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) loanTableModel.getValueAt(selectedRow, 1);
            Book book = bookController.getBookById(bookId);

            if (book != null && book.isLoaned()) {
                loanController.returnBook(book); // Mark as returned
                refreshLoanTable(); // Refresh the table
                JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "The selected book is not loaned!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a loan to return.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshLoanTable() {
        loanTableModel.setRowCount(0);
        List<Loan> loans = loanController.getLoans();
        for (Loan loan : loans) {
            Book book = bookController.getBookById(loan.getBookId());
            loanTableModel.addRow(new Object[]{
                    loan.getLoanId(),
                    loan.getBookId(),
                    book != null ? book.getTitle() : "Unknown",
                    loan.getUserId(),
                    loan.getLoanDate(),
                    loan.getReturnDate() != null ? loan.getReturnDate() : "Not Returned"
            });
        }
    }
}
