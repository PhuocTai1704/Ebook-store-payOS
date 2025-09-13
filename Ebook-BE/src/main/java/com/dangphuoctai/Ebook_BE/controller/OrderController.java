package com.dangphuoctai.Ebook_BE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dangphuoctai.Ebook_BE.payloads.dto.Link;
import com.dangphuoctai.Ebook_BE.payloads.response.OrderInfo;
import com.dangphuoctai.Ebook_BE.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderInfo> postMethodName(HttpServletRequest request, @RequestBody List<Long> bookIds) {
        OrderInfo result = orderService.createOrder(request, bookIds);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/orders/{orderId}/links")
    public ResponseEntity<List<Link>> getLinkByOrderCode(@PathVariable Long orderId, HttpServletRequest request) {
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(),
                request.getServerPort());
        List<Link> links = orderService.getLinksByOrderId(orderId, baseUrl);
        return ResponseEntity.ok(links);
    }
}
