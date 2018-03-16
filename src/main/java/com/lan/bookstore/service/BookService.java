package com.lan.bookstore.service;

import com.lan.bookstore.domain.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    Book findOne(Long id);

    List<Book> findByCategory(String category);

    List<Book> blurrySearch(String keyword);

    Book save(Book book);

    void removeOne(Long id);
}
