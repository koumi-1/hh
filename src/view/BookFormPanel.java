package view;

import javax.swing.*;
import java.awt.*;

public class BookFormPanel extends JPanel {
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField genreField;
    private final JTextField yearField;

    public BookFormPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        add(titleField, gbc);

        // Author Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Author:"), gbc);
        authorField = new JTextField(20);
        gbc.gridx = 1;
        add(authorField, gbc);

        // Genre Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Genre:"), gbc);
        genreField = new JTextField(20);
        gbc.gridx = 1;
        add(genreField, gbc);

        // Year Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Year:"), gbc);
        yearField = new JTextField(20);
        gbc.gridx = 1;
        add(yearField, gbc);
    }

    // Getters for retrieving input
    public String getTitle() {
        return titleField.getText().trim();
    }

    public String getAuthor() {
        return authorField.getText().trim();
    }

    public String getGenre() {
        return genreField.getText().trim();
    }

    public String getYear() {
        return yearField.getText().trim();
    }

    // Setters for populating fields
    public void setTitle(String title) {
        titleField.setText(title);
    }

    public void setAuthor(String author) {
        authorField.setText(author);
    }

    public void setGenre(String genre) {
        genreField.setText(genre);
    }

    public void setYear(String year) {
        yearField.setText(year);
    }
}
