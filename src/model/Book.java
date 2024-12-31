package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isLoaned;

    public boolean isLoaned() {
        return isLoaned;
    }
}
