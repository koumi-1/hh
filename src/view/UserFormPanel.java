package view;

import javax.swing.*;
import java.awt.*;

public class UserFormPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField roleField;
    private final JPasswordField passwordField;

    public UserFormPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Role Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Role:"), gbc);
        roleField = new JTextField(20);
        gbc.gridx = 1;
        add(roleField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);
    }

    public String getName() {
        return nameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getRole() {
        return roleField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setEmail(String email) {
        emailField.setText(email);
    }

    public void setRole(String role) {
        roleField.setText(role);
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }
}
