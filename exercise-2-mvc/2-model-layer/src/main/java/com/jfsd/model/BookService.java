package com.jfsd.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business rules of the model layer. A controller would call these methods;
 * it should never have to repeat the checks.
 */
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public void addBook(Book book) {
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (repository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("A book with ISBN " + book.getIsbn() + " already exists");
        }
        repository.save(book);
    }

    public List<Book> listBooks() {
        return repository.findAll();
    }

    public List<Book> findByAuthor(String author) {
        return repository.findAll().stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public void applyDiscount(String isbn, int percent) {
        if (percent <= 0 || percent >= 100) {
            throw new IllegalArgumentException("Discount must be between 1 and 99 percent");
        }
        Book book = repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No book with ISBN " + isbn));
        book.setPrice(book.getPrice() * (100 - percent) / 100.0);
        repository.save(book);
    }

    public double totalValue() {
        return repository.findAll().stream().mapToDouble(Book::getPrice).sum();
    }
}
