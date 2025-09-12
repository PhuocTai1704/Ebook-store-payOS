package com.dangphuoctai.Ebook_BE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dangphuoctai.Ebook_BE.payloads.dto.BookDTO;
import com.dangphuoctai.Ebook_BE.payloads.response.BookResponse;
import com.dangphuoctai.Ebook_BE.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<BookResponse> getAllBooks(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "bookId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC", required = false) String sortOrder) {
        BookResponse bookResponse = bookService.getAllBooks(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(@RequestParam("image") MultipartFile image,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute BookDTO book) throws Exception {
        BookDTO createdBook = bookService.createBook(book, image, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@RequestParam("image") MultipartFile image,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute BookDTO book) throws Exception {
        BookDTO updatedBook = bookService.updateBook(book, image, file);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
