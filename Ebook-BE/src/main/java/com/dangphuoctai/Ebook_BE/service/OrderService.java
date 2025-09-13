package com.dangphuoctai.Ebook_BE.service;

import java.util.List;

import com.dangphuoctai.Ebook_BE.payloads.dto.Link;
import com.dangphuoctai.Ebook_BE.payloads.response.OrderInfo;

import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {

    OrderInfo createOrder(HttpServletRequest request, List<Long> bookIds);

    List<Link> getLinksByOrderId(Long orderId, String baseUrl);

}
