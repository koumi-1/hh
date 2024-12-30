package view;

import controller.BookController;
import controller.LoanController;
import model.Book;
import model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReturnedBooksPanel extends JPanel {
    private final DefaultTableModel returnedBooksTableModel;
    private final JTable returnedBooksTable;

    public ReturnedBooksPanel(BookController bookController, LoanController loanController) {
        setLayout(new BorderLayout());

        // Table Setup
        String[] columns = {"Loan ID", "Book ID", "Title", "Author", "Loan Date", "Return Date", "User ID"};
        returnedBooksTableModel = new DefaultTableModel(columns, 0);
        returnedBooksTable = new JTable(returnedBooksTableModel);
        returnedBooksTable.setRowHeight(25);

        add(new JScrollPane(returnedBooksTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listener
        refreshButton.addActionListener(e -> refreshReturnedBooksTable(loanController, bookController));

        // Initial Load
        refreshReturnedBooksTable(loanController, bookController);
    }

    private void refreshReturnedBooksTable(LoanController loanController, BookController bookController) {
        returnedBooksTableModel.setRowCount(0); // Clear the table

        List<Loan> loans = loanController.getLoans();
        for (Loan loan : loans) {
            if (loan.getReturnDate() != null) { // Only show returned books
                Book book = bookController.getBookById(loan.getBookId());
                if (book != null) {
                    returnedBooksTableModel.addRow(new Object[]{
                            loan.getLoanId(),
                            loan.getBookId(),
                            book.getTitle(),
                            book.getAuthor(),
                            loan.getLoanDate(),
                            loan.getReturnDate(),
                            loan.getUserId()
                    });
                }
            }
        }
    }
}
