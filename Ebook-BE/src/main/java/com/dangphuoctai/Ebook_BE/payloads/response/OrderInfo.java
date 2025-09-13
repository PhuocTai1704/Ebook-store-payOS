package com.dangphuoctai.Ebook_BE.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private String orderCode;
    private String paymentUrl;
}
