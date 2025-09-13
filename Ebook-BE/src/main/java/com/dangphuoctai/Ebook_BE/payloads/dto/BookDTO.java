package com.dangphuoctai.Ebook_BE.payloads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long bookId;

    private String title;
    private String description;
    private String image;

    private int price;
    private int discount;

}
