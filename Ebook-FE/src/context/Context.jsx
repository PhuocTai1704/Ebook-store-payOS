import React, { createContext, useContext, useState } from "react";

const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [cartList, setCartList] = useState([]);

  const addCart = (bookId) => {
    const existingItem = cartList.find((cartItem) => cartItem === bookId);

    if (!existingItem) {
      setCartList([...cartList, bookId]);
    } else {
      alert("Sản phẩm đã có trong giỏ hàng");
    }
  };

  // Xóa item khỏi danh sách theo bookId
  const removeCart = (bookId) => {
    const newList = cartList.filter((item) => item !== bookId);
    setCartList(newList);
  };

  // Xóa tất cả
  const clearAllCart = () => {
    setCartList([]);
  };

  // Cập nhật danh sách mới
  const setCarts = (items) => {
    setCartList(items);
  };

  // Lấy số lượng phần tử
  const getCount = () => {
    return cartList.length;
  };

  // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
  const isInCart = (bookId) => {
    return cartList.some((item) => item.bookId === bookId);
  };

  const value = {
    cartList,
    addCart,
    removeCart,
    clearAllCart,
    setCarts,
    getCount,
    isInCart,
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};

// Custom hook để sử dụng context
export const useCartList = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error("useCartList must be used within a CartProvider");
  }
  return context;
};

export default CartContext;
