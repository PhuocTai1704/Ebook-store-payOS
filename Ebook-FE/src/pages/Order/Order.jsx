import React, { useState, useEffect } from "react";
import { useCartList } from "../../context/Context";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Order.css";
import { getBookById, createOrder } from "../../services/api";

const Order = () => {
  const navigate = useNavigate();
  const { cartList, clearAllCart, getCount } = useCartList();
  const [bookList, setBookList] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  // Form data
  const [formData, setFormData] = useState({
    email: "",
  });

  const [errors, setErrors] = useState({});

  // Redirect nếu giỏ hàng trống
  useEffect(() => {
    if (cartList.length === 0) {
      navigate("/cart");
    }
  }, [cartList, navigate]);

  const formatPrice = (price) => {
    if (typeof price !== "number" || isNaN(price)) return "0";
    return price.toLocaleString("vi-VN");
  };

  const getFinalPrice = (price, discount) => {
    const p = Number(price) || 0;
    const d = Number(discount) || 0;

    if (d > 0) {
      const discountAmount = (p * d) / 100;
      return p - discountAmount;
    }
    return p;
  };

  const getBookList = async () => {
    try {
      let total = 0;
      const promises = cartList.map(async (bookId) => {
        const response = await getBookById(bookId);
        total += (response.price * (100 - response.discount)) / 100;
        return response;
      });
      const books = await Promise.all(promises);
      setBookList(books);
      setTotal(total);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getBookList();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.email.trim()) {
      newErrors.email = "Vui lòng nhập email";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email không hợp lệ";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const orderData = {
        email: formData.email,
        bookIds: cartList,
      };

      const response = await createOrder(orderData);
      window.open(response.paymentUrl, "_seft");

      // Clear cart after successful order
      clearAllCart();
    } catch (error) {
      console.error("Order failed:", error);
      alert("Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  if (cartList.length === 0) {
    return (
      <div className="order-empty">
        <div className="container py-5">
          <div className="row justify-content-center">
            <div className="col-lg-6 text-center">
              <div className="empty-icon">
                <i className="fas fa-shopping-cart fa-5x text-muted"></i>
              </div>
              <h3 className="mt-4 mb-3">Giỏ hàng trống</h3>
              <p className="text-muted mb-4">
                Bạn chưa có sản phẩm nào trong giỏ hàng.
              </p>
              <Link to="/" className="btn btn-primary btn-lg">
                <i className="fas fa-arrow-left me-2"></i>
                Tiếp tục mua sắm
              </Link>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="order-page">
      <div className="container-fluid py-4">
        <div className="row">
          <div className="col-12">
            <div className="order-header mb-4">
              <h2 className="order-title">
                <i className="fas fa-credit-card me-2"></i>
                Thanh toán
              </h2>
              <div className="breadcrumb-nav">
                <Link to="/" className="text-decoration-none">
                  Trang chủ
                </Link>
                <span className="mx-2">/</span>
                <Link to="/cart" className="text-decoration-none">
                  Giỏ hàng
                </Link>
                <span className="mx-2">/</span>
                <span className="text-muted">Thanh toán</span>
              </div>
            </div>
          </div>
        </div>

        <div className="row">
          {/* Form thông tin khách hàng */}
          <div className="col-lg-8">
            <div className="order-form-section">
              <div className="card">
                <div className="card-header">
                  <h5 className="mb-0">
                    <i className="fas fa-user me-2"></i>
                    Thông tin khách hàng
                  </h5>
                </div>
                <div className="card-body">
                  <form onSubmit={handleSubmit}>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="email" className="form-label">
                          Email <span className="text-danger">*</span>
                        </label>
                        <input
                          type="email"
                          className={`form-control ${
                            errors.email ? "is-invalid" : ""
                          }`}
                          id="email"
                          name="email"
                          value={formData.email}
                          onChange={handleInputChange}
                          placeholder="Nhập email"
                        />
                        {errors.email && (
                          <div className="invalid-feedback">{errors.email}</div>
                        )}
                      </div>

                      <div className="col-md-6 mb-3">
                        <label htmlFor="paymentMethod" className="form-label">
                          Phương thức thanh toán
                        </label>
                        <select
                          className="form-select"
                          id="paymentMethod"
                          name="paymentMethod"
                        >
                          <option value="credit_card">VietQR của PayOS</option>
                        </select>
                      </div>
                    </div>

                    <div className="d-flex justify-content-between mt-4">
                      <Link to="/cart" className="btn btn-outline-secondary">
                        <i className="fas fa-arrow-left me-2"></i>
                        Quay lại giỏ hàng
                      </Link>

                      <button
                        type="submit"
                        className="btn btn-primary btn-lg"
                        disabled={loading}
                      >
                        {loading ? (
                          <>
                            <span
                              className="spinner-border spinner-border-sm me-2"
                              role="status"
                              aria-hidden="true"
                            ></span>
                            Đang xử lý...
                          </>
                        ) : (
                          <>
                            <i className="fas fa-credit-card me-2"></i>
                            Hoàn tất thanh toán
                          </>
                        )}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>

          {/* Tóm tắt đơn hàng */}
          <div className="col-lg-4">
            <div className="order-summary-section">
              <div className="card sticky-top">
                <div className="card-header">
                  <h5 className="mb-0">
                    <i className="fas fa-receipt me-2"></i>
                    Tóm tắt đơn hàng
                  </h5>
                </div>
                <div className="card-body">
                  {/* Danh sách sản phẩm */}
                  <div className="order-items">
                    {bookList.map((item) => (
                      <div key={item.bookId} className="order-item">
                        <div className="d-flex">
                          <img
                            src={item.image}
                            alt={item.title}
                            className="order-item-image"
                          />
                          <div className="order-item-details">
                            <h6 className="order-item-title">{item.title}</h6>
                            <div className="order-item-price">
                              {formatPrice(
                                getFinalPrice(item.price, item.discount)
                              )}{" "}
                              VNĐ
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>

                  <hr />

                  {/* Tổng kết */}
                  <div className="order-summary">
                    <div className="summary-row">
                      <span>Số lượng sách:</span>
                      <span>{getCount()}</span>
                    </div>
                    <div className="summary-row">
                      <span>Tạm tính:</span>
                      <span>{formatPrice(total)} VNĐ</span>
                    </div>
                    <div className="summary-row">
                      <span>Phí xử lý:</span>
                      <span className="text-success">Miễn phí</span>
                    </div>
                    <hr />
                    <div className="summary-row total">
                      <span>Tổng cộng:</span>
                      <span className="total-price">
                        {formatPrice(total)} VNĐ
                      </span>
                    </div>
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

export default Order;
