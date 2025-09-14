import React, { useState, useEffect } from "react";
import { useCartList } from "../../context/Context";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Cart.css";
import { getBookById, IMG_URL } from "../../services/api";

const Cart = () => {
  const navigate = useNavigate();
  const [bookList, setBookList] = useState([]);
  const [total, setTotal] = useState(0);
  const { cartList, removeCart, clearAllCart, getCount } = useCartList();

  const handleCheckout = () => {
    if (cartList.length === 0) {
      alert("Giỏ hàng trống!");
      return;
    }
    navigate("/order");
  };

  const formatPrice = (price) => {
    if (typeof price !== "number" || isNaN(price)) return "0";
    return price.toLocaleString("vi-VN");
  };

  // Hàm tính giá cuối sau giảm
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
      // Mảng các promise
      const promises = cartList.map(async (bookId) => {
        const response = await getBookById(bookId);
        total += (response.price * (100 - response.discount)) / 100;
        return response;
      });
      // Chờ tất cả promise hoàn thành
      const books = await Promise.all(promises);
      setBookList(books);
      setTotal(total);
      console.log(books, total);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getBookList();
  }, []);

  if (cartList.length === 0) {
    return (
      <div className="cart-empty">
        <div className="container-fluid py-5">
          <div className="row justify-content-center">
            <div className="col-lg-6 text-center">
              <div className="empty-cart-icon">
                <i className="fas fa-shopping-cart fa-5x text-muted"></i>
              </div>
              <h3 className="mt-4 mb-3">Giỏ hàng trống</h3>
              <p className="text-muted mb-4">
                Bạn chưa có sản phẩm nào trong giỏ hàng. Hãy khám phá và thêm
                sản phẩm yêu thích!
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
    <div className="cart-page ">
      <div className="container-fluid py-4">
        <div className="row">
          <div className="col-12">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2 className="cart-title">
                <i className="fas fa-shopping-cart me-2"></i>
                Giỏ hàng của bạn
              </h2>
              <div className="cart-summary">
                <span className="badge bg-primary fs-6">{getCount()} sách</span>
              </div>
            </div>
          </div>
        </div>

        <div className="row">
          {/* Danh sách sản phẩm */}
          <div className="col-lg-8">
            <div className="cart-items">
              {bookList.map((item) => (
                <div key={item.bookId} className="cart-item">
                  <div className="row align-items-center">
                    <div className="col-md-2">
                      <img
                        src={IMG_URL + item.image}
                        alt={item.title}
                        className="cart-item-image"
                      />
                    </div>
                    <div className="col-md-6">
                      <h5 className="cart-item-title">{item.title}</h5>
                      {item.discount > 0 && (
                        <span className="badge bg-danger">
                          -{item.discount}%
                        </span>
                      )}
                    </div>
                    <div className="col-md-2">
                      <div className="cart-item-price">
                        <div className="price">
                          {formatPrice(
                            getFinalPrice(item.price, item.discount)
                          )}
                          VNĐ
                        </div>
                        {item.discount > 0 && (
                          <div className="original-price">
                            {formatPrice(item.price)} VNĐ
                          </div>
                        )}
                      </div>
                    </div>
                    <div className="col-md-2">
                      <div className="cart-item-actions">
                        <button
                          className="btn btn-outline-danger"
                          onClick={() => removeCart(item.bookId)}
                        >
                          <i className="fas fa-trash me-1"></i>
                          Xóa
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Actions */}
            <div className="cart-actions mt-4">
              <div className="d-flex justify-content-between">
                <Link to="/" className="btn btn-outline-primary">
                  <i className="fas fa-arrow-left me-2"></i>
                  Tiếp tục mua sắm
                </Link>
                <button
                  className="btn btn-outline-danger"
                  onClick={clearAllCart}
                >
                  <i className="fas fa-trash me-2"></i>
                  Xóa tất cả
                </button>
              </div>
            </div>
          </div>

          {/* Tóm tắt đơn hàng */}
          <div className="col-lg-4">
            <div className="cart-summary-card">
              <div className="card">
                <div className="card-header">
                  <h5 className="mb-0">
                    <i className="fas fa-receipt me-2"></i>
                    Tóm tắt đơn hàng
                  </h5>
                </div>
                <div className="card-body">
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
                  <div className="d-grid gap-2 mt-4">
                    <button
                      className="btn btn-primary btn-lg"
                      onClick={handleCheckout}
                    >
                      <i className="fas fa-credit-card me-2"></i>
                      Thanh toán
                    </button>
                  </div>
                </div>
              </div>

              {/* Thông tin ebook */}
              <div className="ebook-info mt-3">
                <div className="card">
                  <div className="card-header">
                    <h6 className="mb-0">
                      <i className="fas fa-info-circle me-2"></i>
                      Thông tin ebook
                    </h6>
                  </div>
                  <div className="card-body">
                    <ul className="list-unstyled mb-0">
                      <li className="mb-2">
                        <i className="fas fa-download text-primary me-2"></i>
                        Tải xuống ngay sau khi thanh toán
                      </li>
                      <li className="mb-2">
                        <i className="fas fa-mobile-alt text-primary me-2"></i>
                        Đọc trên mọi thiết bị
                      </li>
                      <li className="mb-2">
                        <i className="fas fa-infinity text-primary me-2"></i>
                        Sở hữu vĩnh viễn
                      </li>
                      <li>
                        <i className="fas fa-shield-alt text-primary me-2"></i>
                        Bảo hành trọn đời
                      </li>
                    </ul>
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

export default Cart;
