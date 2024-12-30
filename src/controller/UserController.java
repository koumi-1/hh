package controller;

import model.User;
import utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static final String USERS_FILE = "resources/users.csv";
    private final List<User> users;

    public UserController() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    public List<User> getUsers() {
        this.loadUsersFromFile();
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        saveUsersToFile();
    }

    public User getUserById(int id) {
        this.loadUsersFromFile();
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public void deleteUser(int userId) {
        users.removeIf(user -> user.getId() == userId);
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
        users.addAll(CSVUtils.readCSV(USERS_FILE).stream()
                .map(row -> new User(
                        Integer.parseInt(row[0]), // ID
                        row[1],                  // Name
                        row[2],                  // Email
                        row[3],                  // Role
                        row.length > 4 ? row[4] : "" // Password (optional for legacy rows)
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
        CSVUtils.writeCSV(USERS_FILE, data);
    }
}
