import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use(
  (config) => {
    console.log("Making request to:", config.url);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.error("API request failed:", error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// Hàm getBooks với phân trang và sắp xếp
export const getBooks = async (params = {}) => {
  const {
    pageNumber = 0,
    pageSize = 5,
    sortBy = "bookId",
    sortOrder = "ASC",
  } = params;

  const response = await apiClient.get("/books", {
    params: {
      pageNumber,
      pageSize,
      sortBy,
      sortOrder,
    },
  });
  console.log(response.data);
  return response.data;
};

export const getBookById = async (id) => {
  const response = await apiClient.get(`/books/${id}`);
  return response.data;
};

export const downloadBook = async (fileName) => {
  const response = await apiClient.get(`/books/download/${fileName}`, {
    responseType: "blob",
  });
  return response.data;
};

export const createOrder = async (orderData) => {
  const response = await apiClient.post("/orders", orderData);
  return response.data;
};

export const getOrderLinks = async (orderId) => {
  const response = await apiClient.get(`/orders/${orderId}/links`);
  return response.data;
};

// Export all functions
export default {
  getBooks,
  getBookById,
  downloadBook,
  createOrder,
  getOrderLinks,
};
