package com.dangphuoctai.Ebook_BE.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.dangphuoctai.Ebook_BE.payloads.dto.BookDTO;
import com.dangphuoctai.Ebook_BE.payloads.response.BookResponse;

public interface BookService {

    BookDTO getBookById(Long bookId);

    BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    BookDTO createBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException;

    BookDTO updateBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException;

    void deleteBook(Long bookId);

    Resource downloadBookFile(String fileName) throws Exception;

}
