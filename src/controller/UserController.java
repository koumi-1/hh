package controller;

import model.User;
import utils.CSVUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserController {
//    Paths.get(Main.class.getResource("/books.csv").getPath());
    private static final String USERS_FILE = Paths.get(UserController.class.getResource("/books.csv").getPath()).toString();
//    private static final String USERS_FILE_ = "resources/users.csv";
    private final List<User> users;

    public UserController() {
        System.out.println(USERS_FILE);
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public boolean addUser(String name, String email, String role, String password) {
        this.loadUsersFromFile();
        if (emailExists(email)) return false; // Prevent duplicate emails
        int newId = users.size() + 1;
        users.add(new User(newId, name, email, role, password));
        saveUsersToFile();
        return true;
    }

    public boolean editUser(int id, String name, String email, String role, String password) {
        this.loadUsersFromFile();
        User user = getUserById(id);
        if (user != null) {
            if (!user.getEmail().equalsIgnoreCase(email) && emailExists(email)) return false; // Prevent duplicate emails
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);
            user.setPassword(password);
            saveUsersToFile();
            return true;
        }
        return false;
    }

    public void deleteUser(int id) {
        this.loadUsersFromFile();
        users.removeIf(user -> user.getId() == id);
        saveUsersToFile();
    }

    public User authenticate(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean emailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private void loadUsersFromFile() {
        this.users.clear();
        users.addAll(CSVUtils.readFromCSV(USERS_FILE).stream()
                .map(row -> new User(
                        Integer.parseInt(row[0]), // ID
                        row[1],                  // Name
                        row[2],                  // Email
                        row[3],                  // Role
                        row[4]                   // Password
                ))
                .toList());
    }

    private void saveUsersToFile() {
        List<String[]> data = users.stream()
                .map(user -> new String[]{
                        String.valueOf(user.getId()),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getPassword()
                })
                .toList();
        CSVUtils.writeToCSV(USERS_FILE, data);
    }
}
