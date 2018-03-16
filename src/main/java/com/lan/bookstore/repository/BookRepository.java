package com.lan.bookstore.repository;

import com.lan.bookstore.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
	List<Book> findByCategory(String category);
	
	List<Book> findByTitleContaining(String title);
}
