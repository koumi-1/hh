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
    private final DefaultTableModel loanedBooksTableModel;
    private final JTable loanedBooksTable;

    public LoanedBooksPanel(BookController bookController, LoanController loanController) {
        setLayout(new BorderLayout());

        // Table Setup
        String[] columns = {"Loan ID", "Book ID", "Title", "Author", "Loan Date", "User ID"};
        loanedBooksTableModel = new DefaultTableModel(columns, 0);
        loanedBooksTable = new JTable(loanedBooksTableModel);
        loanedBooksTable.setRowHeight(25);

        add(new JScrollPane(loanedBooksTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listener
        refreshButton.addActionListener(e -> refreshLoanedBooksTable(loanController, bookController));

        // Initial Load
        refreshLoanedBooksTable(loanController, bookController);
    }

    private void refreshLoanedBooksTable(LoanController loanController, BookController bookController) {
        loanedBooksTableModel.setRowCount(0); // Clear the table

        List<Loan> loans = loanController.getLoans();
        for (Loan loan : loans) {
            if (loan.getReturnDate() == null) { // Only show currently loaned books
                Book book = bookController.getBookById(loan.getBookId());
                if (book != null) {
                    loanedBooksTableModel.addRow(new Object[]{
                            loan.getLoanId(),
                            loan.getBookId(),
                            book.getTitle(),
                            book.getAuthor(),
                            loan.getLoanDate(),
                            loan.getUserId()
                    });
                }
            }
        }
    }
}
