package com.dangphuoctai.Ebook_BE.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.dangphuoctai.Ebook_BE.service.FileService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private FileService fileService;

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
    public ResponseEntity<BookDTO> createBook(
            @RequestParam(name = "fileImage") MultipartFile image,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @ModelAttribute BookDTO book) throws Exception {
        BookDTO createdBook = bookService.createBook(book, image, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/books")
    public ResponseEntity<BookDTO> updateBook(@RequestParam(name = "fileImage", required = false) MultipartFile image,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @ModelAttribute BookDTO book) throws Exception {
        BookDTO updatedBook = bookService.updateBook(book, image, file);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/image/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageName) throws FileNotFoundException {
        InputStream imageStream = bookService.getImage(imageName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("inline", imageName);

        return new ResponseEntity<>(new InputStreamResource(imageStream), headers, HttpStatus.OK);
    }

    @GetMapping("/books/download/{fileName}")
    public ResponseEntity<Resource> downloadBookFile(@PathVariable String fileName) throws Exception {
        Resource fileData = bookService.downloadBookFile(fileName);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileData.getFilename() + "\"")
                .body(fileData);
    }

}
