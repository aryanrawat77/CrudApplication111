package com.example.SimplestCRUDExample.controller;

import com.example.SimplestCRUDExample.model.Book;
import com.example.SimplestCRUDExample.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/getAllBooks")
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> bookPage = bookRepository.findAll(pageable);

            if (bookPage.hasContent()) {
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookObj = bookRepository.findById(id);
        if (bookObj.isPresent()) {
            return new ResponseEntity<>(bookObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            Book bookObj = bookRepository.save(book);
            return new ResponseEntity<>(bookObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Optional<Book> bookData = bookRepository.findById(id);
            if (bookData.isPresent()) {
                Book updatedBookData = bookData.get();
                updatedBookData.setTitle(book.getTitle());
                updatedBookData.setAuthor(book.getAuthor());
                updatedBookData.setStatus(book.getStatus()); // Also update status if needed
                updatedBookData.setPublishedDate(book.getPublishedDate()); // Also update date if needed

                Book bookObj = bookRepository.save(updatedBookData);
                return new ResponseEntity<>(bookObj, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        try {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAllBooks")
    public ResponseEntity<HttpStatus> deleteAllBooks() {
        try {
            bookRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New endpoints for filtering with pagination

    @GetMapping("/getBooksByStatus")
    public ResponseEntity<Page<Book>> getBooksByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> bookPage = bookRepository.findByStatus(status, pageable);

            if (bookPage.hasContent()) {
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooksByPublishedDate")
    public ResponseEntity<Page<Book>> getBooksByPublishedDate(
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            LocalDate publishedDate = LocalDate.parse(date);
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> bookPage = bookRepository.findByPublishedDateAfter(publishedDate, pageable);

            if (bookPage.hasContent()) {
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooksByStatusAndDate")
    public ResponseEntity<Page<Book>> getBooksByStatusAndDate(
            @RequestParam String status,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            LocalDate publishedDate = LocalDate.parse(date);
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> bookPage = bookRepository.findByStatusAndPublishedDateAfter(status, publishedDate, pageable);

            if (bookPage.hasContent()) {
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
