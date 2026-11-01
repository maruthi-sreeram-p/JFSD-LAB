package com.jfsd.model;

import java.util.List;
import java.util.Optional;

/**
 * Data access part of the model. The service only talks to this interface,
 * so the storage (memory, file, database) can be swapped later.
 */
public interface BookRepository {

    void save(Book book);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findAll();

    boolean deleteByIsbn(String isbn);
}
