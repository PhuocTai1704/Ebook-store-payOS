package com.dangphuoctai.Ebook_BE.payloads.dto;

import java.util.List;

import com.dangphuoctai.Ebook_BE.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;

    private List<OrderItemDTO> orderItems;
    private int totalAmount;

    private OrderStatus status;
    private String orderDateTime;
}
