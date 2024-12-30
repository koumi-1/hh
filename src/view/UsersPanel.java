package view;

import controller.LoanController;
import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsersPanel extends JPanel {
    private final DefaultTableModel userTableModel;
    private final JTable userTable;

    public UsersPanel(UserController userController, LoanController loanController) {
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Email", "Role"};
        userTableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(25);
        refreshUserTable(userController);

        add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton viewLoanHistoryButton = new JButton("View Loan History");

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(viewLoanHistoryButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addUserButton.addActionListener(e -> handleAddUser(userController));
        editUserButton.addActionListener(e -> handleEditUser(userController));
        deleteUserButton.addActionListener(e -> handleDeleteUser(userController));
        viewLoanHistoryButton.addActionListener(e -> handleViewLoanHistory(loanController, userController));
    }

private void handleAddUser(UserController userController) {
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
                User user = new User(userController.getUsers().size() + 1, name, email, role, password);
                userController.addUser(user);
                refreshUserTable(userController);
            }
        }
    }

    private void handleEditUser(UserController userController) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);
            User user = userController.getUserById(userId);

            if (user != null) {
                UserFormPanel formPanel = new UserFormPanel();
                formPanel.setName(user.getName());
                formPanel.setEmail(user.getEmail());
                formPanel.setRole(user.getRole());

                int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit User", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    user.setName(formPanel.getName());
                    user.setEmail(formPanel.getEmail());
                    user.setRole(formPanel.getRole());
                    refreshUserTable(userController);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteUser(UserController userController) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);
            userController.getUsers().removeIf(user -> user.getId() == userId);
            refreshUserTable(userController);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleViewLoanHistory(LoanController loanController, UserController userController) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTableModel.getValueAt(selectedRow, 0);
            User user = userController.getUserById(userId);

            if (user != null) {
                StringBuilder history = new StringBuilder("Loan History for " + user.getName() + ":\n\n");
                loanController.getLoansByUserId(userId).forEach(loan -> {
                    history.append("Loan ID: ").append(loan.getLoanId())
                           .append(", Book ID: ").append(loan.getBookId())
                           .append(", Loan Date: ").append(loan.getLoanDate())
                           .append(", Return Date: ").append(loan.getReturnDate() != null ? loan.getReturnDate() : "Not Returned")
                           .append("\n");
                });
                JOptionPane.showMessageDialog(this, history.toString(), "Loan History", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to view loan history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUserTable(UserController userController) {
        userTableModel.setRowCount(0);
        for (User user : userController.getUsers()) {
            userTableModel.addRow(new Object[]{user.getId(), user.getName(), user.getEmail(), user.getRole()});
        }
    }
}
