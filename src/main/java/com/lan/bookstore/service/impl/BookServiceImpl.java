package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.Book;
import com.lan.bookstore.repository.BookRepository;
import com.lan.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
		List<Book> bookList = (List<Book>) bookRepository.findAll();
		List<Book> activeBookList = new ArrayList<>();

		for (Book book : bookList) {
			if (book.isActive()) {
				activeBookList.add(book);
			}
		}

		return activeBookList;
	}

	public Book findOne(Long id) {
		return bookRepository.findById(id).get();
	}

	public List<Book> findByCategory(String category) {
		List<Book> bookList = bookRepository.findByCategory(category);

		List<Book> activeBookList = new ArrayList<>();

		for (Book book : bookList) {
			if (book.isActive()) {
				activeBookList.add(book);
			}
		}

		return activeBookList;
	}

	public List<Book> blurrySearch(String title) {
		// Elastic search
		List<Book> bookList = bookRepository.findByTitleContaining(title);

		List<Book> activeBookList = new ArrayList<>();

		for (Book book : bookList) {
			if (book.isActive()) {
				activeBookList.add(book);
			}
		}

		return activeBookList;
	}
	
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	public void removeOne(Long id) {
		bookRepository.deleteById(id);
	}
}
