import React, { useState, useEffect } from "react";
import { getBooks, IMG_URL } from "../../services/api";
import { useCartList } from "../../context/Context";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Home.css";

const Home = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Phân trang state
  const [currentPage, setCurrentPage] = useState(0); // API sử dụng pageNumber bắt đầu từ 0
  const [booksPerPage] = useState(6); // Số sách mỗi trang
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Sử dụng Context để quản lý giỏ hàng
  const { addCart, getCount, isInCart } = useCartList();

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        setLoading(true);

        // Gọi API với phân trang
        const data = await getBooks({
          pageNumber: currentPage,
          pageSize: booksPerPage,
          sortBy: "bookId",
          sortOrder: "ASC",
        });

        setBooks(data.content || data); // Fallback nếu API không trả về Pageable
        setTotalPages(
          data.totalPages ||
            Math.ceil((data.content?.length || data.length) / booksPerPage)
        );
        setTotalElements(
          data.totalElements || data.content?.length || data.length
        );
        setLoading(false);
      } catch (err) {
        console.error("Error fetching books:", err);
        setError("Không thể tải danh sách sách");
        setLoading(false);
      }
    };

    fetchBooks();
  }, [currentPage, booksPerPage]);

  // Helper function to format price from cents to VND
  const formatPrice = (priceInCents) => {
    return priceInCents.toLocaleString("vi-VN");
  };

  // Helper function to calculate final price (with discount if applicable)
  const getFinalPrice = (price, discount) => {
    if (discount > 0) {
      const originalPrice = price;
      const discountAmount = (originalPrice * discount) / 100;
      return (originalPrice - discountAmount).toLocaleString("vi-VN");
    }
    return formatPrice(price);
  };

  // Hàm xử lý thêm vào giỏ hàng - gọi addCart từ Context
  const handleAddToCart = (book) => {
    addCart(book.bookId);
  };

  // Hàm chuyển trang
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Hàm chuyển trang trước/sau
  const goToPreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  const goToNextPage = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(currentPage + 1);
    }
  };

  // Component phân trang
  const Pagination = () => {
    const pageNumbers = [];

    // Tính toán số trang hiển thị (chuyển đổi từ 0-based sang 1-based cho UI)
    const displayCurrentPage = currentPage + 1;
    const displayTotalPages = totalPages;

    let startPage = Math.max(1, displayCurrentPage - 2);
    let endPage = Math.min(displayTotalPages, displayCurrentPage + 2);

    // Điều chỉnh để luôn hiển thị 5 trang nếu có thể
    if (endPage - startPage < 4) {
      if (startPage === 1) {
        endPage = Math.min(displayTotalPages, startPage + 4);
      } else if (endPage === displayTotalPages) {
        startPage = Math.max(1, endPage - 4);
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      pageNumbers.push(i);
    }

    return (
      <nav aria-label="Phân trang sách">
        <ul className="pagination justify-content-center">
          {/* Nút Trang trước */}
          <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
            <button
              className="page-link"
              onClick={goToPreviousPage}
              disabled={currentPage === 0}
            >
              <i className="fas fa-chevron-left"></i>
            </button>
          </li>

          {/* Trang đầu */}
          {startPage > 1 && (
            <>
              <li className="page-item">
                <button className="page-link" onClick={() => paginate(0)}>
                  1
                </button>
              </li>
              {startPage > 2 && (
                <li className="page-item disabled">
                  <span className="page-link">...</span>
                </li>
              )}
            </>
          )}

          {/* Các trang số */}
          {pageNumbers.map((number) => (
            <li
              key={number}
              className={`page-item ${
                displayCurrentPage === number ? "active" : ""
              }`}
            >
              <button
                className="page-link"
                onClick={() => paginate(number - 1)} // Chuyển về 0-based cho API
              >
                {number}
              </button>
            </li>
          ))}

          {/* Trang cuối */}
          {endPage < displayTotalPages && (
            <>
              {endPage < displayTotalPages - 1 && (
                <li className="page-item disabled">
                  <span className="page-link">...</span>
                </li>
              )}
              <li className="page-item">
                <button
                  className="page-link"
                  onClick={() => paginate(displayTotalPages - 1)}
                >
                  {displayTotalPages}
                </button>
              </li>
            </>
          )}

          {/* Nút Trang sau */}
          <li
            className={`page-item ${
              currentPage === totalPages - 1 ? "disabled" : ""
            }`}
          >
            <button
              className="page-link"
              onClick={goToNextPage}
              disabled={currentPage === totalPages - 1}
            >
              <i className="fas fa-chevron-right"></i>
            </button>
          </li>
        </ul>
      </nav>
    );
  };

  if (loading) {
    return (
      <div className="home-loading-container">
        <div className="text-center">
          <div className="spinner-border text-primary mb-3" role="status">
            <span className="visually-hidden">Đang tải...</span>
          </div>
          <h4>Đang tải sách...</h4>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="home-error-container">
        <div className="text-center">
          <div className="alert alert-danger" role="alert">
            <h4 className="alert-heading">Ôi! Có lỗi xảy ra</h4>
            <p>{error}</p>
            <hr />
            <button
              className="btn btn-outline-danger"
              onClick={() => window.location.reload()}
            >
              Thử lại
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="home-container">
      {/* Header with Cart */}
      <header className="bg-white shadow-sm border-bottom sticky-top">
        <div className="container">
          <nav className="navbar navbar-expand-lg navbar-light py-3">
            <div className="navbar-brand fw-bold fs-3 text-primary">
              <i className="fas fa-book me-2"></i>Thư Viện Số
            </div>

            <div className="navbar-nav ms-auto">
              <div className="nav-item">
                <Link
                  to="/cart"
                  className="btn btn-outline-primary position-relative"
                >
                  <i className="fas fa-shopping-cart me-2"></i>
                  Giỏ hàng
                  {getCount() > 0 && (
                    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                      {getCount()}
                    </span>
                  )}
                </Link>
              </div>
            </div>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-primary text-white home-hero-section">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6">
              <h1 className="display-4 fw-bold mb-4">Thư Viện Số</h1>
              <p className="lead mb-4">
                Khám phá hàng nghìn cuốn sách điện tử và mở rộng kiến thức với
                bộ sưu tập phong phú của chúng tôi
              </p>
              <div className="row text-center">
                <div className="col-4">
                  <div className="border-end">
                    <h3 className="fw-bold text-warning">{totalElements}+</h3>
                    <small>Sách có sẵn</small>
                  </div>
                </div>
                <div className="col-4">
                  <div className="border-end">
                    <h3 className="fw-bold text-warning">24/7</h3>
                    <small>Truy cập</small>
                  </div>
                </div>
                <div className="col-4">
                  <h3 className="fw-bold text-warning">Tức thì</h3>
                  <small>Tải xuống</small>
                </div>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="text-center">
                <i className="fas fa-book-open display-1 text-warning"></i>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Books Section */}
      <section className="home-books-section">
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="display-5 fw-bold">Sách Nổi Bật</h2>
            <p className="lead text-muted">
              Những lựa chọn được tuyển chọn dành cho bạn
            </p>
            <p className="text-muted">
              Trang {currentPage + 1} / {totalPages} - Hiển thị {books.length} /{" "}
              {totalElements} sách
            </p>
          </div>

          <div className="home-books-grid">
            {books.length > 0 ? (
              books.map((book) => (
                <div key={book.bookId} className="home-book-card">
                  <div className="card h-100 shadow-sm border-0">
                    <div className="home-book-image-container">
                      <img
                        src={IMG_URL + book.image}
                        className="card-img-top h-100 object-fit-cover"
                        alt={book.title}
                        onError={(e) => {
                          e.target.src =
                            "https://via.placeholder.com/300x400/ddd/999?text=No+Image";
                        }}
                      />
                      {book.discount > 0 && (
                        <div className="home-discount-badge">
                          -{book.discount}%
                        </div>
                      )}
                    </div>

                    <div className="home-card-body d-flex flex-column">
                      <h5 className="home-book-title">{book.title}</h5>
                      <p className="home-book-description flex-grow-1">
                        {book.description}
                      </p>

                      <div className="home-price-button-container">
                        <div className="home-final-price">
                          {getFinalPrice(book.price, book.discount)} VNĐ
                        </div>
                        <button
                          className={`btn btn-primary home-btn-add-cart ${
                            isInCart(book.bookId) ? "disabled" : ""
                          }`}
                          onClick={() => handleAddToCart(book)}
                          disabled={isInCart(book.bookId)}
                        >
                          {isInCart(book.bookId) ? (
                            <>
                              <i className="fas fa-check me-2"></i>
                              Đã thêm
                            </>
                          ) : (
                            <>
                              <i className="fas fa-shopping-cart me-2"></i>
                              Thêm vào giỏ hàng
                            </>
                          )}
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="home-empty-state">
                <i className="fas fa-book"></i>
                <h4>Không có sách nào</h4>
                <p>Hiện tại chưa có sách nào trong danh mục này.</p>
              </div>
            )}
          </div>

          {/* Phân trang */}
          {totalPages > 1 && (
            <div className="home-pagination-container">
              <Pagination />
            </div>
          )}
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-light home-footer border-top">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h5 className="fw-bold">Thư Viện Số</h5>
              <p className="text-muted small">
                Cổng thông tin tri thức của bạn thông qua sách điện tử
              </p>
            </div>
            <div className="col-md-6 text-md-end">
              <div className="d-flex justify-content-md-end gap-3">
                <a href="#" className="text-muted">
                  <i className="fab fa-facebook-f"></i>
                </a>
                <a href="#" className="text-muted">
                  <i className="fab fa-twitter"></i>
                </a>
                <a href="#" className="text-muted">
                  <i className="fab fa-instagram"></i>
                </a>
                <a href="#" className="text-muted">
                  <i className="fab fa-linkedin-in"></i>
                </a>
              </div>
            </div>
          </div>
          <hr />
          <div className="text-center">
            <p className="text-muted small mb-0">
              &copy; 2024 Thư Viện Số. Tất cả quyền được bảo lưu.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;
