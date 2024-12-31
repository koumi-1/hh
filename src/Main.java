package main;

import controller.*;
import view.LoginView;
import view.LibraryManagementView;

public class Main {
    public static void main(String[] args) {
        // Initialize Controllers
        BookController bookController = new BookController();
        UserController userController = new UserController();
        LoanController loanController = new LoanController(bookController, userController);
        // Initialize LoginController
        LoginController loginController = new LoginController(userController);

        // Launch Login View
        new LoginView(loginController, () -> {
            new LibraryController(bookController, userController, loanController);
        });
    }
}
