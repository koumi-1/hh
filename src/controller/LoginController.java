package controller;

import model.User;

public class LoginController {
    private final UserController userController;

    public LoginController(UserController userController) {
        this.userController = userController;
    }

    public boolean authenticate(String email, String password) {
        for (User user : userController.getUsers()) {
            if (userController.authenticate(email, password) != null) return true;
        }
        return false;
    }
}
