package com.jfsd.model;

/**
 * Uses the model layer on its own - no view and no controller are needed
 * to test it, which is one of the benefits of MVC.
 */
public class ModelDemo {

    public static void main(String[] args) {
        BookService service = new BookService(new InMemoryBookRepository());

        service.addBook(new Book("B101", "Head First Java", "Kathy Sierra", 650));
        service.addBook(new Book("B102", "Effective Java", "Joshua Bloch", 720));
        service.addBook(new Book("B103", "Java Concurrency", "Brian Goetz", 800));
        service.addBook(new Book("B104", "Java Puzzlers", "Joshua Bloch", 540));

        System.out.println("All books:");
        service.listBooks().forEach(System.out::println);

        System.out.println();
        System.out.println("Books by Joshua Bloch:");
        service.findByAuthor("Joshua Bloch").forEach(System.out::println);

        System.out.println();
        service.applyDiscount("B103", 10);
        System.out.println("After 10% discount on B103:");
        System.out.println(service.listBooks().get(2));

        System.out.printf("%nTotal value of the catalogue: %.2f%n", service.totalValue());

        // the business rules protect the data
        System.out.println();
        try {
            service.addBook(new Book("B101", "Duplicate", "Someone", 100));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            service.addBook(new Book("B105", "Free Book", "Someone", 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
