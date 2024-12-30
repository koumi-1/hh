package model;

import lombok.Data;

@Data
public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isLoaned;

    public Book(int id, String title, String author, String genre, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.isLoaned = false;
    }

    public boolean isLoaned() {
        return isLoaned;
    }
}
