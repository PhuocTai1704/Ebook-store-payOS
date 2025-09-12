package com.dangphuoctai.Ebook_BE.service.Impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dangphuoctai.Ebook_BE.payloads.dto.BookDTO;
import com.dangphuoctai.Ebook_BE.payloads.response.BookResponse;
import com.dangphuoctai.Ebook_BE.service.BookService;
import com.dangphuoctai.Ebook_BE.service.FileService;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public BookDTO getBookById(Long bookId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException {
        String fileName = fileService.uploadImage(path, image);

        return null;
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteBook(Long bookId) {
        // TODO Auto-generated method stub

    }
}
