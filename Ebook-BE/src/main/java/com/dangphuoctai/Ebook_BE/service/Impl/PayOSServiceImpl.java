package com.dangphuoctai.Ebook_BE.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dangphuoctai.Ebook_BE.entity.Order;
import com.dangphuoctai.Ebook_BE.enums.OrderStatus;
import com.dangphuoctai.Ebook_BE.exeptions.APIException;
import com.dangphuoctai.Ebook_BE.exeptions.ResourceNotFoundException;
import com.dangphuoctai.Ebook_BE.payloads.dto.OrderItemDTO;
import com.dangphuoctai.Ebook_BE.repository.OrderRepo;
import com.dangphuoctai.Ebook_BE.service.PayOSService;

import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Service
public class PayOSServiceImpl implements PayOSService {

    @Autowired
    private PayOS payOS;

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public String createPayment(String webhookUrl, Long orderId, int amount, List<OrderItemDTO> orderItems) {
        try {
            List<ItemData> items = orderItems.stream()
                    .map(item -> ItemData.builder()
                            .name(item.getBook().getTitle())
                            .quantity(1)
                            .price(item.getPrice())
                            .build())
                    .toList();
            PaymentData paymentData = PaymentData.builder().orderCode(orderId).amount(amount)
                    .description("Thanh toán đơn hàng Ebook").returnUrl(webhookUrl + "/success")
                    .cancelUrl(webhookUrl + "/cancel")
                    .items(items).build();
            CheckoutResponseData result = payOS.createPaymentLink(paymentData);

            return result.getCheckoutUrl(); // Return the payment URL from PayOS

        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("Không thể tạo thanh toán với PayOS");
        }
    }

    @Override
    public void confirmWebhook(String webhookUrl) {
        try {
            String verifiedWebhookUrl = payOS.confirmWebhook(webhookUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("Xác nhận webhook với PayOS thất bại: " + e.getMessage());
        }
    }

    @Override
    public void verifiedPaymentWithPayOS(Webhook webhook) {
        try {
            WebhookData webhookData = payOS.verifyPaymentWebhookData(webhook);
            if ("00".equals(webhookData.getCode())) {
                Long orderId = webhookData.getOrderCode();
                Order order = orderRepo.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
                if (order.getTotalAmount() < webhookData.getAmount()) {
                    throw new APIException("Số tiền thanh toán không khớp với tổng số tiền của đơn hàng");
                } else {
                    order.setStatus(OrderStatus.COMPLETED);
                    orderRepo.save(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("Xác thực thanh toán với PayOS thất bại: " + e.getMessage());
        }
    }

    @Override
    public boolean verifiedPaymentWithOrderCode(Long orderCode, int totalAmount) {
        try {
            PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(orderCode);
            if ("PAID".equals(paymentLinkData.getStatus())) {
                if (totalAmount < paymentLinkData.getAmount()) {
                    throw new APIException("Số tiền thanh toán không khớp với tổng số tiền của đơn hàng");
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("Xác thực thanh toán với PayOS thất bại: " + e.getMessage());
        }
    }

}
