package controller;

import model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookController {
    private final List<Book> books = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(int bookId) {
        books.removeIf(book -> book.getId() == bookId);
    }

    public Book getBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }
}
