package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsersPanel extends JPanel {
    private final DefaultTableModel userTableModel;
    private final JTable userTable;
    private final UserController userController;

    public UsersPanel(UserController userController) {
        this.userController = userController;

        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Email", "Role"};
        userTableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(25);
        refreshUserTable();

        add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addUserButton.addActionListener(e -> handleAddUser());
        editUserButton.addActionListener(e -> handleEditUser());
        deleteUserButton.addActionListener(e -> handleDeleteUser());
    }

    private void handleAddUser() {
        UserFormPanel formPanel = new UserFormPanel();
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add User", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = formPanel.getName();
            String email = formPanel.getEmail();
            String role = formPanel.getRole();
            String password = formPanel.getPassword();

            if (name.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean success = userController.addUser(name, email, role, password);
                if (!success) {
                    JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    refreshUserTable();
                }
            }
        }
    }

    private void handleEditUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);
            User user = userController.getUserById(userId);

            if (user != null) {
                UserFormPanel formPanel = new UserFormPanel();
                formPanel.setName(user.getName());
                formPanel.setEmail(user.getEmail());
                formPanel.setRole(user.getRole());
                formPanel.setPassword(user.getPassword());

                int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit User", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String name = formPanel.getName();
                    String email = formPanel.getEmail();
                    String role = formPanel.getRole();
                    String password = formPanel.getPassword();

                    if (name.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean success = userController.editUser(userId, name, email, role, password);
                        if (!success) {
                            JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            refreshUserTable();
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);
            userController.deleteUser(userId);
            refreshUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUserTable() {
        userTableModel.setRowCount(0);
        for (User user : userController.getUsers()) {
            userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            });
        }
    }
}
