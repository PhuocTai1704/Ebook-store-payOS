package com.dangphuoctai.Ebook_BE.utils;

import java.util.List;

import com.dangphuoctai.Ebook_BE.exeptions.APIException;
import com.dangphuoctai.Ebook_BE.payloads.dto.Link;
import com.dangphuoctai.Ebook_BE.payloads.dto.OrderDTO;
import com.dangphuoctai.Ebook_BE.payloads.dto.OrderItemDTO;

public class Email {

    public static String getEmailBody(OrderDTO order, List<Link> links) {
        if (order == null || links == null || links.isEmpty()) {
            throw new APIException("Order or links cannot be null or empty");
        }

        StringBuilder sb = new StringBuilder();

        // HTML Header với styling đẹp
        sb.append("<!DOCTYPE html>")
                .append("<html lang='vi'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
                .append("<style>")
                .append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }")
                .append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }")
                .append(".header h1 { margin: 0; font-size: 28px; font-weight: 300; }")
                .append(".content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }")
                .append(".order-info { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }")
                .append(".order-info h2 { color: #667eea; margin-top: 0; font-size: 20px; }")
                .append(".info-row { display: flex; justify-content: space-between; margin: 10px 0; padding: 8px 0; border-bottom: 1px solid #eee; }")
                .append(".info-row:last-child { border-bottom: none; }")
                .append(".info-label { font-weight: 600; color: #555; }")
                .append(".info-value { color: #333; }")
                .append(".products { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }")
                .append(".product-item { padding: 15px; border: 1px solid #e9ecef; border-radius: 6px; margin: 10px 0; background: #f8f9fa; }")
                .append(".product-title { font-weight: 600; color: #495057; margin-bottom: 5px; }")
                .append(".product-price { color: #28a745; font-weight: 600; }")
                .append(".total { background: #e3f2fd; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0; }")
                .append(".total-amount { font-size: 24px; font-weight: 700; color: #1976d2; }")
                .append(".download-links { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }")
                .append(".download-link { display: block; padding: 12px 16px; margin: 8px 0; background: #28a745; color: white; text-decoration: none; border-radius: 6px; transition: background 0.3s; }")
                .append(".download-link:hover { background: #218838; }")
                .append(".footer { text-align: center; margin-top: 30px; padding: 20px; color: #6c757d; font-size: 14px; }")
                .append(".contact-info { background: #fff3cd; padding: 15px; border-radius: 6px; border-left: 4px solid #ffc107; margin: 20px 0; }")
                .append("</style>")
                .append("</head>")
                .append("<body>");

        // Header với logo và tiêu đề
        sb.append("<div class='header'>")
                .append("<h1>�� Ebook Store</h1>")
                .append("<p style='margin: 10px 0 0 0; opacity: 0.9;'>Cảm ơn bạn đã mua hàng!</p>")
                .append("</div>");

        // Nội dung chính
        sb.append("<div class='content'>")
                .append("<div style='text-align: center; margin-bottom: 30px;'>")
                .append("<h2 style='color: #667eea; margin: 0;'>🎉 Đơn hàng của bạn đã được xác nhận!</h2>")
                .append("<p style='color: #6c757d; margin: 10px 0;'>Chúng tôi đã nhận được đơn hàng và đang chuẩn bị sách cho bạn.</p>")
                .append("</div>");

        // Thông tin đơn hàng
        sb.append("<div class='order-info'>")
                .append("<h2>📋 Thông tin đơn hàng</h2>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Mã đơn hàng:</span>")
                .append("<span class='info-value'><strong>#").append(order.getOrderId()).append("</strong></span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Ngày mua:</span>")
                .append("<span class='info-value'>").append(order.getOrderDateTime()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Trạng thái:</span>")
                .append("<span class='info-value'><span style='background: #d4edda; color: #155724; padding: 4px 8px; border-radius: 4px;'>")
                .append(order.getStatus()).append("</span></span>")
                .append("</div>")
                .append("</div>");

        // Chi tiết sản phẩm
        sb.append("<div class='products'>")
                .append("<h2>�� Chi tiết sản phẩm</h2>");

        for (OrderItemDTO item : order.getOrderItems()) {
            sb.append("<div class='product-item'>")
                    .append("<div class='product-title'>").append(item.getBook().getTitle()).append("</div>")
                    .append("<div class='product-price'>💰 ").append(String.format("%,d", item.getPrice()))
                    .append(" VND</div>")
                    .append("</div>");
        }
        sb.append("</div>");

        // Tổng tiền
        sb.append("<div class='total'>")
                .append("<h3 style='margin: 0 0 10px 0; color: #495057;'>Tổng thanh toán</h3>")
                .append("<div class='total-amount'>").append(String.format("%,d", order.getTotalAmount()))
                .append(" VND</div>")
                .append("</div>");

        // Link tải sách
        sb.append("<div class='download-links'>")
                .append("<h2>⬇️ Link tải sách</h2>")
                .append("<p style='color: #6c757d; margin-bottom: 20px;'>Nhấp vào các link bên dưới để tải sách về máy:</p>");

        for (Link link : links) {
            sb.append("<a href='").append(link.getUrl()).append("' class='download-link'>")
                    .append("📥 ").append(link.getTitle())
                    .append("</a>");
        }
        sb.append("</div>");

        // Thông tin liên hệ
        sb.append("<div class='contact-info'>")
                .append("<h3 style='margin: 0 0 10px 0; color: #856404;'>💬 Cần hỗ trợ?</h3>")
                .append("<p style='margin: 0; color: #856404;'>Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua email: <strong>support@ebookstore.com</strong></p>")
                .append("</div>");

        // Footer
        sb.append("<div class='footer'>")
                .append("<p>Trân trọng,<br><strong>Ebook Store Team</strong></p>")
                .append("<p style='font-size: 12px; margin-top: 20px;'>© 2024 Ebook Store. Tất cả quyền được bảo lưu.</p>")
                .append("</div>");

        sb.append("</div>") // End content
                .append("</body>")
                .append("</html>");

        return sb.toString();
    }
}