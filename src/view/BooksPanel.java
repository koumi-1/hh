package view;

import controller.BookController;
import controller.LoanController;
import controller.UserController;
import model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class BooksPanel extends JPanel {
    private final DefaultTableModel bookTableModel;
    private final JTable bookTable;
    private final UserController userController;
    private final BookController bookController;
    private final LoanController loanController;
    private final JTextField searchField;

    public BooksPanel(UserController userController, BookController bookController, LoanController loanController) {
        this.userController = userController;
        this.bookController = bookController;
        this.loanController = loanController;

        setLayout(new BorderLayout());

        String[] columns = {"ID", "Title", "Author", "Genre", "Year", "Loaned"};
        bookTableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(bookTableModel);
        bookTable.setRowHeight(25);
        refreshBookTable(bookController.getBooks());

        // Search Bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Add Scrollable Table
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
        searchButton.addActionListener(e -> handleSearch());
        addBookButton.addActionListener(e -> handleAddBook());
        editBookButton.addActionListener(e -> handleEditBook());
        deleteBookButton.addActionListener(e -> handleDeleteBook());
        loanBookButton.addActionListener(e -> handleLoanBook());
    }

    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            refreshBookTable(bookController.getBooks());
        } else {
            List<Book> filteredBooks = bookController.getBooks().stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query)
                            || book.getAuthor().toLowerCase().contains(query)
                            || book.getGenre().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            refreshBookTable(filteredBooks);
        }
    }

    private void handleLoanBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            Book book = bookController.getBookById(bookId);

            if (book != null && !book.isLoaned()) {
                String userIdInput = JOptionPane.showInputDialog(this, "Enter User ID to Loan Book:");
                try {
                    int userId = Integer.parseInt(userIdInput);

                    loanController.loanBook(bookId, userId); // Record the loan
                    book.setLoaned(true);
                    bookController.updateBook(book); // Update book status
                    refreshBookTable(bookController.getBooks());

                    JOptionPane.showMessageDialog(this, "Book loaned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid User ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Book is already loaned or unavailable!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to loan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddBook() {
        BookFormPanel formPanel = new BookFormPanel();
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = formPanel.getTitle();
            String author = formPanel.getAuthor();
            String genre = formPanel.getGenre();
            String yearText = formPanel.getYear();

            try {
                int year = Integer.parseInt(yearText);
                if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    bookController.addBook(new Book(
                            bookController.getBooks().size() + 1,
                            title,
                            author,
                            genre,
                            year,
                            false
                    ));
                    refreshBookTable(bookController.getBooks());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditBook() {
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
                    String title = formPanel.getTitle();
                    String author = formPanel.getAuthor();
                    String genre = formPanel.getGenre();
                    String yearText = formPanel.getYear();

                    try {
                        int year = Integer.parseInt(yearText);
                        if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            book.setTitle(title);
                            book.setAuthor(author);
                            book.setGenre(genre);
                            book.setYear(year);
                            bookController.updateBook(book);
                            refreshBookTable(bookController.getBooks());
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            bookController.deleteBook(bookId);
            refreshBookTable(bookController.getBooks());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
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
