package com.dangphuoctai.Ebook_BE.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dangphuoctai.Ebook_BE.entity.Book;
import com.dangphuoctai.Ebook_BE.entity.Order;
import com.dangphuoctai.Ebook_BE.entity.OrderItem;
import com.dangphuoctai.Ebook_BE.enums.OrderStatus;
import com.dangphuoctai.Ebook_BE.exeptions.APIException;
import com.dangphuoctai.Ebook_BE.exeptions.ResourceNotFoundException;
import com.dangphuoctai.Ebook_BE.payloads.dto.Link;
import com.dangphuoctai.Ebook_BE.payloads.dto.OrderItemDTO;
import com.dangphuoctai.Ebook_BE.payloads.request.RequestOrder;
import com.dangphuoctai.Ebook_BE.payloads.response.OrderInfo;
import com.dangphuoctai.Ebook_BE.repository.BookRepo;
import com.dangphuoctai.Ebook_BE.repository.OrderRepo;
import com.dangphuoctai.Ebook_BE.service.OrderService;
import com.dangphuoctai.Ebook_BE.service.PayOSService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private PayOSService payOSService;

    @Autowired
    private ModelMapper modelMapper;

    private final String urlDownload = "/api/books/download/";

    @Override
    public OrderInfo createOrder(HttpServletRequest request, RequestOrder orderRequest) {
        Order order = new Order();
        List<Long> bookIds = orderRequest.getBookIds();
        List<Book> books = bookRepo.findAllById(bookIds);
        if (books.size() != bookIds.size()) {
            throw new APIException("Không tìm thấy một số sách");
        }
        List<OrderItem> orderItems = books.stream().map(book -> {
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setOrder(order);
            item.setPrice(book.getPrice() * (100 - book.getDiscount()) / 100);
            return item;
        }).toList();
        int totalAmount = orderItems.stream().mapToInt(OrderItem::getPrice).sum();
        order.setEmail(orderRequest.getEmail());
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        orderRepo.save(order);
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(item -> modelMapper.map(item, OrderItemDTO.class))
                .toList();
        String linkPayment = payOSService.createPayment(getBaseUrlRequest(request), order.getOrderId(), totalAmount,
                orderItemDTOs);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderCode(order.getOrderId().toString());
        orderInfo.setPaymentUrl(linkPayment);

        return orderInfo;
    }

    @Transactional
    @Override
    public List<Link> getLinksByOrderId(Long orderId, String baseUrl) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if (order.getStatus() != OrderStatus.COMPLETED) {
            boolean check = payOSService.verifiedPaymentWithOrderCode(orderId, order.getTotalAmount());
            if (check) {
                order.setStatus(OrderStatus.COMPLETED);
                orderRepo.save(order);
            } else {
                throw new APIException("Đơn hàng chưa được thanh toán thành công");
            }
        }
        List<Link> links = order.getOrderItems().stream()
                .map(item -> {
                    Link link = new Link();
                    link.setTitle(item.getBook().getTitle());
                    link.setUrl(baseUrl + urlDownload + item.getBook().getFileUrl());
                    return link;
                })
                .toList();

        return links;
    }

    private String getBaseUrlRequest(HttpServletRequest request) {
        System.out.println(request.getHeader("Referer").toString());

        return request.getHeader("Origin").toString();
    }
}
