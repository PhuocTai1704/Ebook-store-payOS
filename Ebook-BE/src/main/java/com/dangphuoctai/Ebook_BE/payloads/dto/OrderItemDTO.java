package com.dangphuoctai.Ebook_BE.payloads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;

    private BookDTO book;
    private int quantity;
    private int price;

}
