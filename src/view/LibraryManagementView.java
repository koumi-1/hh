package view;

import controller.BookController;
import controller.LoanController;
import controller.UserController;
import controller.LibraryController;

import javax.swing.*;
import java.awt.*;

public class LibraryManagementView extends JFrame {
    public LibraryManagementView(LibraryController controller, BookController bookController, UserController userController, LoanController loanController) {
        setTitle("Library Management System");
        setSize(1200, 700);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", new BooksPanel(bookController, loanController));
        tabbedPane.addTab("Users", new UsersPanel(userController, loanController));
        tabbedPane.addTab("Loaned Books", new LoanedBooksPanel(bookController, loanController));
        tabbedPane.addTab("Returned Books", new ReturnedBooksPanel(bookController, loanController));

        add(tabbedPane, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
