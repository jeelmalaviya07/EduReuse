# 📚 EduReuse – Book Swap, Sell & Donate Platform

EduReuse is a full-stack web application that helps students **swap, sell, or donate** books.  
It provides an organized and affordable way to access study materials, reducing waste and promoting reuse.

---

## 🚀 Tech Stack

- **Frontend**: React, Axios, Context API  
- **Backend**: Spring Boot, Spring Security (Basic Auth), JPA/Hibernate  
- **Database**: MySQL  
- **Build Tools**: Maven, npm  

---

## ✨ Features

- 🔐 **Authentication**: Secure login using Basic Authentication  
- 📖 **Book Management**: Add, view, and manage book sets  
- 🔄 **Book Swapping**: Send and receive swap requests between users  
- 💰 **Selling**: Sell books at affordable prices  
- 🎁 **Donating**: Donate books to others in need  
- 🏙️ **User Profiles**: View seller/donor details (name, city, state)  
- ⚙️ **Error Handling**: Graceful fallbacks if data fetching fails  

---

## 🖥️ Frontend Routes (React)

| Route                | Description                                 |
|-----------------------|---------------------------------------------|
| `/`                  | Homepage – list available book sets         |
| `/login`             | User login page                             |
| `/register`          | User registration page                      |
| `/booksets/add`      | Add a new book set                          |
| `/booksets/:id`      | View details of a book set                  |
| `/swap-requests`     | View my swap requests (sent & received)     |
| `/profile`           | View and edit my profile                    |
| `/donate`            | Donate books                                |
| `/sell`              | Sell books                                  |

---

## 🔗 Backend API Endpoints (Spring Boot)

### 👤 Authentication & Users
- `POST /api/auth/register` → Register new user  
- `GET /api/users/me` → Get current logged-in user (requires `Authorization` header)  
- `GET /api/users/{id}` → Get user by ID  

### 📚 Book Sets
- `POST /api/booksets` → Add a book set  
- `GET /api/booksets` → Get all book sets  
- `GET /api/booksets/{id}` → Get a specific book set by ID  
- `GET /api/users/booksets/withSellerId` → Get book sets with seller info  

### 🔄 Swap Requests
- `POST /api/swap-requests` → Create a swap request  
- `GET /api/swap-requests/{userId}` → Get all swap requests for a user  
- `PUT /api/swap-requests/{id}` → Update swap request status (accept/reject)  

### 💰 Sell & 🎁 Donate
- `POST /api/sell` → Create a sell listing  
- `GET /api/sell` → List all books for sale  
- `POST /api/donate` → Create a donation listing  
- `GET /api/donate` → List all donations  

---

## ⚙️ Setup Instructions

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
