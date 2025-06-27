# 🛍️ EasyShop E-Commerce API

Welcome to **EasyShop**, a modern RESTful e-commerce backend API built with **Spring Boot** and **MySQL**.  
This project powers all the backend logic for a pre-built front-end shopping site — handling everything from browsing products to managing user profiles, carts, and orders.

---

💡 About This Project
This project was built as my final capstone for the Year Up United Code Academy. Starting from a provided front-end, I was responsible for developing a full-featured e-commerce backend API using Java, Spring Boot, and MySQL.

🔧 Key Features I Designed and Built:
✅ A complete shopping cart system with quantity updates, cart persistence, and real-time calculations.

🛒 Order placement and order history, allowing users to securely checkout their cart items and view past purchases.

🔐 Role-based security using Spring Security, protecting endpoints for regular users and administrators.

📦 Full CRUD operations for categories, products, and user profiles — with admin-only access for management actions.

🧱 Clean and modular DAO architecture using JDBC and Spring’s @Component, with a clear separation of concerns.

👤 Authenticated user profile management synced to session-based roles and access.

🔍 API tested and validated using Postman, ensuring all features work independently of the front-end.

🧠 What I Learned:
This wasn’t just about building endpoints — it was about deeply understanding how each layer of a full-stack Java application connects, from database schema to secured REST endpoints. Using Postman helped me test and debug APIs in isolation, reinforcing my backend development skills

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
