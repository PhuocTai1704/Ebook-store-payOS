package com.dangphuoctai.Ebook_BE.payloads.response;

import java.util.List;

import com.dangphuoctai.Ebook_BE.payloads.dto.BookDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private List<BookDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
