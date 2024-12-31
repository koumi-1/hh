package controller;

import model.Book;
import utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    private static final String BOOKS_FILE = "resources/books.csv";
    private final List<Book> books;

    public BookController() {
        books = new ArrayList<>();
        loadBooksFromFile();
    }

    public List<Book> getBooks() {
        loadBooksFromFile();
        return books;
    }

    public void addBook(Book book) {
        loadBooksFromFile();
        books.add(book);
        saveBooksToFile();
    }

    public void deleteBook(int bookId) {
        loadBooksFromFile();
        books.removeIf(book -> book.getId() == bookId);
        saveBooksToFile();
    }

    public Book getBookById(int id) {
        loadBooksFromFile();
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    public void updateBook(Book updatedBook) {
        loadBooksFromFile();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == updatedBook.getId()) {
                books.set(i, updatedBook);
                break;
            }
        }
        saveBooksToFile();
    }

    private void loadBooksFromFile() {
        this.books.clear();
        books.addAll(CSVUtils.readFromCSV(BOOKS_FILE).stream()
                .map(row -> new Book(
                        Integer.parseInt(row[0]), // ID
                        row[1],                  // Title
                        row[2],                  // Author
                        row[3],                  // Genre
                        Integer.parseInt(row[4]), // Year
                        Boolean.parseBoolean(row[5]) // Loaned
                ))
                .toList());
    }

    private void saveBooksToFile() {
        List<String[]> data = books.stream()
                .map(book -> new String[]{
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        String.valueOf(book.getYear()),
                        String.valueOf(book.isLoaned())
                })
                .toList();
        CSVUtils.writeToCSV(BOOKS_FILE, data);
    }
}
