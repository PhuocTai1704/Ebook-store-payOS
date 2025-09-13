package com.dangphuoctai.Ebook_BE.service.Impl;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dangphuoctai.Ebook_BE.entity.Book;
import com.dangphuoctai.Ebook_BE.exeptions.APIException;
import com.dangphuoctai.Ebook_BE.exeptions.ResourceNotFoundException;
import com.dangphuoctai.Ebook_BE.payloads.dto.BookDTO;
import com.dangphuoctai.Ebook_BE.payloads.response.BookResponse;
import com.dangphuoctai.Ebook_BE.repository.BookRepo;
import com.dangphuoctai.Ebook_BE.service.BookService;
import com.dangphuoctai.Ebook_BE.service.FileService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public BookDTO getBookById(Long bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> pageBooks = bookRepo.findAll(pageDetails);
        List<BookDTO> bookDTOs = pageBooks.getContent().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(bookDTOs);
        bookResponse.setPageNumber(pageBooks.getNumber());
        bookResponse.setPageSize(pageBooks.getSize());
        bookResponse.setTotalElements(pageBooks.getTotalElements());
        bookResponse.setTotalPages(pageBooks.getTotalPages());
        bookResponse.setLastPage(pageBooks.isLast());

        return bookResponse;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPrice(bookDTO.getPrice());
        book.setDiscount(bookDTO.getDiscount());

        String fileName = fileService.uploadImage(path, image);
        book.setImage(fileName);
        String filePDF = fileService.uploadFilePDF(path, file);
        book.setFileUrl(filePDF);

        bookRepo.save(book);

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, MultipartFile image, MultipartFile file) throws IOException {
        Book book = bookRepo.findById(bookDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookDTO.getBookId()));
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPrice(bookDTO.getPrice());
        book.setDiscount(bookDTO.getDiscount());
        if (image != null) {
            String fileName = fileService.uploadImage(path, image);
            book.setImage(fileName);
        }
        if (file != null) {
            String filePDF = fileService.uploadFilePDF(path, file);
            book.setFileUrl(filePDF);
        }

        bookRepo.save(book);

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public void deleteBook(Long bookId) {

    }

    @Override
    public Resource downloadBookFile(String fileName) throws Exception {
        return fileService.getFilePDF(path, fileName);
    }
}
