package controller;

import model.User;

public class LoginController {
    private final UserController userController;

    public LoginController(UserController userController) {
        this.userController = userController;
    }

    public boolean authenticate(String email, String password) {
        System.out.println("authenticate");
        for (User user : userController.getUsers()) {
            System.out.printf("for user %s.\n", user.getName());
            if (userController.authenticate(email, password) != null) return true;
        }
        System.out.println("exit");
        return false;
    }
}
