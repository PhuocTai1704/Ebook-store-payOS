import React, { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./OrderSuccess.css";
import { getOrderLinks } from "../../services/api";

const OrderSuccess = () => {
  const [orderLinks, setOrderLinks] = useState([]);
  const [searchParams] = useSearchParams();
  const orderCode = searchParams.get("orderCode");
  useEffect(() => {
    getOrderLinks(orderCode)
      .then((res) => {
        setOrderLinks(res);
        console.log(res);
      })
      .catch((error) => {
        console.error(error);
      });
  }, [orderCode]);

  return (
    <div className="order-success-page">
      <div className="container">
        <div className="w-100 row justify-content-center">
          <div className="col-12">
            <div className="success-card">
              {/* Header */}
              <div className="success-header">
                <div className="success-icon-wrapper">
                  <i className="fas fa-check-circle"></i>
                </div>
                <h1 className="success-title">Thanh toán thành công!</h1>
              </div>

              {/* Content */}
              <div className="success-content">
                <p className="success-description">
                  Cảm ơn bạn đã mua sắm! Đơn hàng của bạn đã được xử lý thành
                  công.
                </p>
                {/* Success Details */}
                <div className="success-details row">
                  <div className="detail-item col-md-4">
                    <div className="detail-icon">
                      <i className="fas fa-envelope"></i>
                    </div>
                    <div className="detail-text">
                      <h6>Email xác nhận</h6>
                      <p>Chúng tôi đã gửi email xác nhận đơn hàng đến bạn</p>
                    </div>
                  </div>

                  <div className="detail-item col-md-4">
                    <div className="detail-icon">
                      <i className="fas fa-download"></i>
                    </div>
                    <div className="detail-text">
                      <h6>Link tải ebook</h6>
                      <p>
                        Bạn sẽ nhận được link tải ebook qua email trong vài phút
                      </p>
                    </div>
                  </div>

                  <div className="detail-item col-md-4">
                    <div className="detail-icon">
                      <i className="fas fa-infinity"></i>
                    </div>
                    <div className="detail-text">
                      <h6>Sở hữu vĩnh viễn</h6>
                      <p>Ebook sẽ thuộc về bạn mãi mãi, không có hạn sử dụng</p>
                    </div>
                  </div>
                </div>
                {/* Download Links Section */}
                {orderLinks && orderLinks.length > 0 && (
                  <div className="download-section">
                    <div className="download-header">
                      <i className="fas fa-download"></i>
                      <span>Tải xuống ebook</span>
                    </div>
                    <p className="download-description">
                      Bấm vào nút bên dưới để tải xuống ebook của bạn
                    </p>
                    <div className="download-links">
                      {orderLinks.map((link, index) => (
                        <div key={index} className="download-item">
                          <div className="download-info">
                            <div className="download-icon">
                              <i className="fas fa-book"></i>
                            </div>
                            <div className="download-details">
                              <h6 className="download-title">{link.title}</h6>
                              <p className="download-subtitle">Ebook PDF</p>
                            </div>
                          </div>
                          <a
                            href={link.url}
                            className="btn btn-download"
                            target="_blank"
                            rel="noopener noreferrer"
                          >
                            <i className="fas fa-download me-2"></i>
                            Tải xuống
                          </a>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {/* Action Buttons */}
                <div className="success-actions">
                  <Link to="/" className="btn btn-primary btn-lg">
                    <i className="fas fa-home me-2"></i>
                    Về trang chủ
                  </Link>

                  <Link to="/" className="btn btn-outline-primary btn-lg">
                    <i className="fas fa-shopping-bag me-2"></i>
                    Tiếp tục mua sắm
                  </Link>
                </div>

                {/* Order Info */}
                <div className="order-info">
                  <h5>Thông tin đơn hàng</h5>
                  <div className="info-grid">
                    <div className="info-item">
                      <span className="info-label">Mã đơn hàng:</span>
                      <span className="info-value">#ORD-2024-001</span>
                    </div>
                    <div className="info-item">
                      <span className="info-label">Thời gian:</span>
                      <span className="info-value">
                        {new Date().toLocaleString("vi-VN")}
                      </span>
                    </div>
                    <div className="info-item">
                      <span className="info-label">Phương thức:</span>
                      <span className="info-value">VietQR PayOS</span>
                    </div>
                  </div>
                </div>

                {/* Support Section */}
                <div className="support-section">
                  <div className="support-header">
                    <i className="fas fa-headset"></i>
                    <span>Cần hỗ trợ?</span>
                  </div>
                  <p className="support-text">
                    Nếu bạn có bất kỳ câu hỏi nào về đơn hàng, chúng tôi sẵn
                    sàng hỗ trợ bạn.
                  </p>
                  <div className="support-methods">
                    <a
                      href="mailto:support@ebookstore.com"
                      className="support-method"
                    >
                      <i className="fas fa-envelope"></i>
                      <span>Email hỗ trợ</span>
                    </a>
                    <a href="tel:19001234" className="support-method">
                      <i className="fas fa-phone"></i>
                      <span>Gọi điện</span>
                    </a>
                    <a href="#" className="support-method">
                      <i className="fab fa-facebook-messenger"></i>
                      <span>Chat trực tuyến</span>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderSuccess;
