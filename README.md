# 🛍️ EasyShop E-Commerce API

Welcome to **EasyShop**, a modern RESTful e-commerce backend API built with **Spring Boot** and **MySQL**.  
This project powers all the backend logic for a pre-built front-end shopping site — handling everything from browsing products to managing user profiles, carts, and orders.

---

## 💡 About This Project

This project started as part of my final capstone for the Year Up United Code Academy. We were provided with a front-end and tasked with building out the full backend API to support real-world e-commerce features.

Along the way, I:

- Faced and solved **security challenges** around user roles and protected endpoints.
- Learned how to structure **modular, testable DAOs** with JDBC and Spring.
- Built a fully functional **cart and order system** from scratch.
- Created CRUD APIs with **admin-level access controls**.
- Integrated **user profile management** that syncs with authenticated sessions.

This wasn’t just about getting it to work — it was about understanding how each layer of a modern Java app connects.

---

## 🚀 Features

### 👤 User & Security
- JWT-based authentication and login system
- Role-based access (`ADMIN`, `USER`)
- Secure endpoints using Spring Security

### 📦 Products & Categories
- Browse all products and filter by category, price, or color
- Admins can create, update, and delete products and categories

### 🛒 Shopping Cart
- Logged-in users can add, update quantity, remove items, or clear cart
- Cart stored in the database for persistence

### 📦 Orders
- Place orders directly from the cart
- Order history saved per user

### 📄 User Profiles
- Authenticated users can view and update their profile details (name, email, phone, address)

---

## 🧪 My Contributions (vs. Base Project)

✅ Implemented full **CRUD for categories and products**  
✅ Built the **shopping cart system** with full persistence  
✅ Developed **order processing** that converts cart → order  
✅ Added **profile management endpoints**  
✅ Solved multiple issues with **security annotations and DAO interactions**  
✅ Enhanced error handling and validation throughout

---

## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JDBC (Data Access)**
- **MySQL**
- **Postman** (for testing)
- Front-End (provided) — Already built and ready to use with your API!

---

## 🏁 Getting Started

### 🧱 Prerequisites
- Java 17+
- Maven
- MySQL Server

### 🛠️ Database Setup

1. Create a database called `easyshop`
2. Run the SQL scripts found in `src/main/resources/db/`
3. Update your `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/easyshop
   spring.datasource.username=your_user
   spring.datasource.password=your_pass
