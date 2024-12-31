package controller;

import view.LibraryManagementView;

    public class LibraryController {
    private final BookController bookController;
    private final UserController userController;
    private final LoanController loanController;

    public LibraryController(BookController bookController, UserController userController, LoanController loanController) {
        this.bookController = bookController;
        this.userController = userController;
        this.loanController = loanController;
        new LibraryManagementView(this, bookController, userController, loanController);
    }
}
