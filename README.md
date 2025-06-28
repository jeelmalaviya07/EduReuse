# 📚 EduReuse – Book Swap, Sell & Donation Platform

EduReuse is a web-based platform that allows users to donate, sell, or swap used school books (classes 1–12). The system supports secure user registration, book and book set listing, and a full swap request system with conditions and validations.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot (Backend)
- Spring Data JPA
- MySQL (Database)
- React.js + Vite (Frontend)
- Axios + React Router
- Spring Security (Basic Auth)

---

## ⚙️ Backend Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/EduReuse.git
   cd EduReuse
   ```

2. **Create a MySQL database**
   - Name: `edureuse_db`

3. **Configure `application.properties`**
   File: `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/edureuse_db
   spring.datasource.username=root
   spring.datasource.password=your_password

   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Run the backend**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## ⚙️ Frontend Setup Instructions

1. Navigate to frontend folder:
   ```bash
   cd EduReuse-Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the frontend:
   ```bash
   npm run dev
   ```

4. Access the app at:
   ```
   http://localhost:5173/
   ```

---

## 🔐 Authentication

- Uses **Basic Auth**
- Authorization header must be set in frontend using base64 encoded credentials:
  ```
  Authorization: Basic base64(username:password)
  ```

---

## 📌 API Endpoints

### 👤 User APIs

| Method | Endpoint                   | Description                     |
|--------|----------------------------|---------------------------------|
| POST   | `/api/users/register`      | Register a new user             |
| GET    | `/api/users/me`            | Get logged-in user              |
| GET    | `/api/users/{id}`          | Get user by ID                  |

---

### 📚 BookSet APIs

| Method | Endpoint                          | Description                             |
|--------|-----------------------------------|-----------------------------------------|
| POST   | `/api/booksets`                   | Add new BookSet                         |
| GET    | `/api/booksets`                   | Get all BookSets                        |
| GET    | `/api/booksets/type/{type}`       | Get BookSets by type (SALE/SWAP/DONATE) |
| GET    | `/api/booksets/user/{userId}`     | Get BookSets by User                    |
| DELETE | `/api/booksets/{id}`              | Delete BookSet by ID                    |

---

### 📕 Book APIs (within BookSet)

| Method | Endpoint                    | Description               |
|--------|-----------------------------|---------------------------|
| POST   | `/api/books`                | Add Book to BookSet       |
| GET    | `/api/books/bookset/{id}`   | Get Books by BookSet ID   |

---

### 🔁 Swap Request APIs

| Method | Endpoint                                                             | Description                                    |
|--------|----------------------------------------------------------------------|------------------------------------------------|
| POST   | `/api/swap-requests/send/{offeredId}/{requestedId}`                 | Send swap request                              |
| GET    | `/api/swap-requests/my-requests/{userId}`                           | View all requests sent by user                 |
| GET    | `/api/swap-requests/received-requests/{userId}`                     | View all requests received by user             |
| POST   | `/api/swap-requests/accept/{requestId}`                             | Accept swap request (auto-removes both sets)   |
| DELETE | `/api/swap-requests/reject/{requestId}`                             | Reject a request                               |
| DELETE | `/api/swap-requests/withdraw/{requestId}`                           | Withdraw a sent request                        |

---

### 💰 Transaction APIs

| Method | Endpoint                      | Description                         |
|--------|-------------------------------|-------------------------------------|
| POST   | `/api/transactions/sale`      | Record a sale transaction           |
| POST   | `/api/transactions/donate`    | Record a donation transaction       |
| GET    | `/api/transactions/user/{id}` | View user transaction history       |

---

## 🧠 Swap Logic Summary

A swap is allowed only if:
- Offered BookSet's `classLevel`, `board`, and `completeSet` match the requested BookSet's desired fields.
- No duplicate swap request exists for same BookSet pair.
- On acceptance, both BookSets are deleted and a transaction is recorded.

---

## 📝 Notes

- `application.properties` is ignored by Git using `.gitignore`
- Email uses Gmail App Password – generate it from: https://myaccount.google.com/apppasswords
- Spring Security is used for all protected endpoints

---
