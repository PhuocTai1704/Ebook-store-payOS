import React from "react";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./OrderCancel.css";

const OrderCancel = () => {
  return (
    <div className="order-cancel-page">
      <div className="container">
        <div className="w-100 row justify-content-center">
          <div className="col-12">
            <div className="cancel-card">
              {/* Header */}
              <div className="cancel-header">
                <div className="cancel-icon-wrapper">
                  <i className="fas fa-times-circle"></i>
                </div>
                <h1 className="cancel-title">Thanh toán bị hủy</h1>
              </div>

              {/* Content */}
              <div className="cancel-content">
                <p className="cancel-description">
                  Quá trình thanh toán đã bị hủy. Đơn hàng của bạn chưa được xử
                  lý.
                </p>

                {/* Action Buttons */}
                <div className="cancel-actions">
                  <Link to="/cart" className="btn btn-primary btn-lg">
                    <i className="fas fa-shopping-cart me-2"></i>
                    Tiếp tục mua sắm
                  </Link>

                  <Link to="/" className="btn btn-outline-primary btn-lg">
                    <i className="fas fa-home me-2"></i>
                    Về trang chủ
                  </Link>
                </div>

                {/* Help Section */}
                <div className="help-section">
                  <div className="help-header">
                    <i className="fas fa-question-circle"></i>
                    <span>Cần hỗ trợ?</span>
                  </div>
                  <p className="help-text">
                    Nếu bạn gặp khó khăn trong quá trình thanh toán, chúng tôi
                    sẵn sàng hỗ trợ bạn 24/7.
                  </p>
                  <div className="contact-methods">
                    <a
                      href="mailto:support@ebookstore.com"
                      className="contact-method"
                    >
                      <i className="fas fa-envelope"></i>
                      <span>Email hỗ trợ</span>
                    </a>
                    <a href="tel:19001234" className="contact-method">
                      <i className="fas fa-phone"></i>
                      <span>Gọi điện</span>
                    </a>
                    <a href="#" className="contact-method">
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

export default OrderCancel;
