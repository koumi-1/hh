package view;

import controller.BookController;
import controller.LoanController;
import controller.UserController;
import model.Book;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BooksPanel extends JPanel {
    private final DefaultTableModel bookTableModel;
    private final JTable bookTable;

    public BooksPanel(BookController bookController, LoanController loanController) {
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Title", "Author", "Genre", "Year", "Loaned"};
        bookTableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookTableModel);
        bookTable.setRowHeight(25);
        refreshBookTable(bookController);

        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBookButton = new JButton("Add Book");
        JButton editBookButton = new JButton("Edit Book");
        JButton deleteBookButton = new JButton("Delete Book");
        JButton loanBookButton = new JButton("Loan Book");

        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(loanBookButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addBookButton.addActionListener(e -> handleAddBook(bookController));
        editBookButton.addActionListener(e -> handleEditBook(bookController));
        deleteBookButton.addActionListener(e -> handleDeleteBook(bookController));
        loanBookButton.addActionListener(e -> handleLoanBook(bookController, loanController));
    }

    private void handleAddBook(BookController bookController) {
        BookFormPanel formPanel = new BookFormPanel();
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = formPanel.getTitle();
            String author = formPanel.getAuthor();
            String genre = formPanel.getGenre();
            try {
                int year = Integer.parseInt(formPanel.getYear());
                if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Book book = new Book(bookController.getBooks().size() + 1, title, author, genre, year);
                    bookController.addBook(book);
                    refreshBookTable(bookController);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditBook(BookController bookController) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            Book book = bookController.getBookById(bookId);

            if (book != null) {
                BookFormPanel formPanel = new BookFormPanel();
                formPanel.setTitle(book.getTitle());
                formPanel.setAuthor(book.getAuthor());
                formPanel.setGenre(book.getGenre());
                formPanel.setYear(String.valueOf(book.getYear()));

                int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    book.setTitle(formPanel.getTitle());
                    book.setAuthor(formPanel.getAuthor());
                    book.setGenre(formPanel.getGenre());
                    try {
                        book.setYear(Integer.parseInt(formPanel.getYear()));
                        refreshBookTable(bookController);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteBook(BookController bookController) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            bookController.deleteBook(bookId);
            refreshBookTable(bookController);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoanBook(BookController bookController, LoanController loanController) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            Book book = bookController.getBookById(bookId);

            if (book != null && !book.isLoaned()) {
                String userIdString = JOptionPane.showInputDialog(this, "Enter User ID for the Loan:");
                if (userIdString != null) {
                    try {
                        int userId = Integer.parseInt(userIdString);
                        User user = new UserController().getUserById(userId); // Fetch user by ID
                        if (user != null) {
                            loanController.loanBook(book, user);
                            refreshBookTable(bookController);
                        } else {
                            JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid User ID!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Book is already loaned!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to loan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshBookTable(BookController bookController) {
        bookTableModel.setRowCount(0);
        for (Book book : bookController.getBooks()) {
            bookTableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getYear(),
                    book.isLoaned() ? "Yes" : "No"
            });
        }
    }
}
