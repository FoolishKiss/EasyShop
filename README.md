# 🛒 EasyShop

A secure, full-stack e-commerce REST API built with **Spring Boot** and **MySQL**. Users can register, authenticate, browse product categories, add items to a shopping cart, and manage them—admin users can manage categories and products.

---

## 🚀 Features

| Feature                 | Description                                                                 |
| ----------------------- | --------------------------------------------------------------------------- |
| 📦 Product Management   | Search and filter by category, price range, and color                       |
| 🧺 Shopping Cart        | Add, update, or remove products from cart (persisted by user)               |
| 🗃️ Category Management | Admins can create, update, and delete product categories                    |
| 🔐 Secure APIs          | Uses Spring Security and role-based permissions (`ROLE_USER`, `ROLE_ADMIN`) |
| 🗃️ MySQL Integration   | Schema and data bootstrapped using `create_database.sql`                    |
| 🧪 Unit Testing         | JUnit-based tests for DAO layer and DB integration                          |

---

## 🛠️ Tech Stack

* **Backend**: Java 17, Spring Boot (Web, Security, Validation, JDBC)
* **Database**: MySQL 8+
* **Authentication**: JWT (JSON Web Token)
* **Build Tool**: Maven
* **Testing**: Spring Boot Test, JUnit, Spring Security Test

---

## 📂 Project Structure

```
capstone-starter/
│
├── src/
│   ├── main/java/org/yearup/...
│   │   ├── controllers/      # REST API endpoints
│   │   ├── data/             # DAO interfaces and MySQL implementations
│   │   ├── models/           # Domain models (User, Product, Cart, etc.)
│   │   └── security/         # JWT filters and Spring Security config
│   └── resources/
│       ├── application.properties
│       └── banner.txt
│
├── test/                    # Unit and integration tests
│
├── database/
│   └── create_database.sql  # SQL schema and seed data
├── pom.xml                  # Maven config
└── mvnw / mvnw.cmd          # Maven Wrapper scripts
```

---

## ⚙️ Getting Started

### ✅ Prerequisites

* Java 17+
* Maven 3.6+
* MySQL 8+

---

## 📬 API Endpoints

### 🔐 Authentication

| Method | Endpoint    | Description              |
| ------ | ----------- | ------------------------ |
| POST   | `/login`    | Login, returns JWT token |

### 📦 Products

| Method | Endpoint         | Description                    |
| ------ | ---------------- | ------------------------------ |
| GET    | `/products`      | Search products (query params) |
| GET    | `/products/{id}` | Get product by ID              |
| POST   | `/products`      | Add product *(Admin only)*     |
| PUT    | `/products/{id}` | Update product *(Admin only)*  |
| DELETE | `/products/{id}` | Delete product *(Admin only)*  |

### 🗂 Categories

| Method | Endpoint                    | Description                    |
| ------ | --------------------------- | ------------------------------ |
| GET    | `/categories`               | Get all categories             |
| GET    | `/categories/{id}`          | Get category by ID             |
| GET    | `/categories/{id}/products` | Get products by category       |
| POST   | `/categories`               | Add category *(Admin only)*    |
| PUT    | `/categories/{id}`          | Update category *(Admin only)* |
| DELETE | `/categories/{id}`          | Delete category *(Admin only)* |

### 🛒 Shopping Cart *(Requires Authentication)*

| Method | Endpoint                     | Description             |
| ------ | ---------------------------- | ----------------------- |
| GET    | `/cart`                      | View user’s cart        |
| POST   | `/cart/products/{productId}` | Add product to cart     |
| PUT    | `/cart/products/{productId}` | Update quantity in cart |
| DELETE | `/cart`                      | Clear cart              |

---

## 🔐 Roles & Permissions

* `ROLE_USER`: Can register, login, view products/categories, and manage own cart
* `ROLE_ADMIN`: Can also manage products and categories (CRUD)

---


## 🧠 Future Enhancements

* Add payment integration (Stripe/PayPal)
* Sign up button on frontend
* Email confirmation on registration
* Order history and invoice generation
* Frontend React/Vue app integration

---


