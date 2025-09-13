package com.dangphuoctai.Ebook_BE.service;

import java.util.List;

import com.dangphuoctai.Ebook_BE.payloads.dto.OrderItemDTO;

import vn.payos.type.Webhook;

public interface PayOSService {

    String createPayment(String webhookUrl, Long orderId, int amount, List<OrderItemDTO> orderItems);

    void confirmWebhook(String webhookUrl);

    void verifiedPaymentWithPayOS(Webhook webhook);

    boolean verifiedPaymentWithOrderCode(Long orderCode, int totalAmount);

}
