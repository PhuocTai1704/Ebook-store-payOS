import { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import Home from "./pages/Home/Home.jsx";
import Cart from "./pages/Cart/Cart.jsx";
import Order from "./pages/Order/Order.jsx";
import { CartProvider } from "./context/Context";
import OrderCancel from "./pages/Order/OrderCancel.jsx";
import OrderSuccess from "./pages/Order/OrderSuccess.jsx";

function App() {
  return (
    <CartProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/order" element={<Order />} />
          <Route path="/order/cancel" element={<OrderCancel />} />
          <Route path="/order/success" element={<OrderSuccess />} />
        </Routes>
      </BrowserRouter>
    </CartProvider>
  );
}

export default App;
