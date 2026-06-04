<div align="center">

<img src="https://img.shields.io/badge/DoConnect-Q%26A%20Platform-4A90E2?style=for-the-badge&logoColor=white" alt="DoConnect" />

<h3>A Stack Overflow-inspired Question & Answer platform built with Spring Boot Microservices</h3>

<p>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-8.x-4479A1?style=flat-square&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/License-Academic-lightgrey?style=flat-square" />
</p>

<p>
  <a href="#-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-setup">Setup</a> •
  <a href="#-api-reference">API Reference</a> •
  <a href="#-security">Security</a>
</p>

</div>

---

## Overview

**DoConnect** is a full-stack Q&A platform built on a Spring Boot microservices architecture. It supports user-driven discussions with questions, answers, likes, comments, and real-time chat — along with a dedicated admin portal for content moderation and email notifications.

---

## ✨ Features

<table>
<tr>
<td width="50%">

**User Portal**
- Register, login, and logout with JWT authentication
- Ask questions and search by keyword
- Post answers on open discussion threads
- Like and comment on answers
- Real-time chat with other users

</td>
<td width="50%">

**Admin Portal**
- Separate admin login
- Approve or reject questions and answers
- Delete inappropriate content
- Deactivate / activate user accounts
- Close discussion threads
- Email alerts for new Q&A activity

</td>
</tr>
</table>

---

## 🏗️ Architecture

DoConnect is structured as five independently deployable microservices behind a single API Gateway, registered with Eureka for service discovery.

```
doconnect/
├── eureka-server/         →  Service Registry      :8761
├── api-gateway/           →  API Gateway           :8765
├── user-service/          →  User features         :8081
├── admin-service/         →  Admin features        :8082
├── notification-service/  →  Email notifications   :8083
└── doconnect-frontend/    →  HTML / CSS / JS
```

**Inter-service communication** is handled via `WebClient`. All services register themselves with Eureka on startup and are routed through the API Gateway.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security + JWT |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Routing | Spring Cloud API Gateway |
| Persistence | Spring Data JPA + MySQL 8.x |
| Email | JavaMailSender (Gmail SMTP) |
| Inter-service | WebClient |
| Frontend | HTML5, CSS3, Vanilla JS (Fetch API) |

---

## 🗄️ Database Schema

```
doconnect_user_db
├── users
├── questions
├── answers
├── likes
├── comments
└── chat_messages

doconnect_admin_db
├── admins
├── question_approval
└── answer_approval

doconnect_notification_db
└── email_logs
```

---

## ⚙️ Setup

### Prerequisites

| Tool | Version |
|---|---|
| Java JDK | 17+ |
| Maven | 3.x |
| MySQL | 8.x |
| Git | latest |

### 1. Clone the repository

```bash
git clone https://github.com/priyankashow1111/doconnect.git
cd doconnect
```

### 2. Create the databases

```sql
CREATE DATABASE doconnect_user_db;
CREATE DATABASE doconnect_admin_db;
CREATE DATABASE doconnect_notification_db;
```

### 3. Configure each service

In `src/main/resources/application.properties` for each service:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_APP_PASSWORD
```

> **Note:** Use a Gmail [App Password](https://support.google.com/accounts/answer/185833), not your regular account password.

### 4. Start services in order

```bash
# 1. Service Registry — must start first
cd eureka-server && mvn spring-boot:run

# 2. API Gateway
cd api-gateway && mvn spring-boot:run

# 3. Application services (order independent)
cd user-service         && mvn spring-boot:run
cd admin-service        && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### 5. Launch the frontend

Open `doconnect-frontend/index.html` using **Live Server** in VS Code.

---

## 📡 API Reference

### User Service — `:8081`

<details>
<summary><strong>Users</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register a new user |
| `POST` | `/api/users/login` | Authenticate and receive JWT |
| `GET` | `/api/users/profile` | Get current user profile |
| `GET` | `/api/users/all` | List all users |
| `PUT` | `/api/users/update/{id}` | Update user details |
| `PUT` | `/api/users/deactivate/{id}` | Deactivate a user |
| `PUT` | `/api/users/activate/{id}` | Activate a user |

</details>

<details>
<summary><strong>Questions</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/questions/ask` | Submit a new question |
| `GET` | `/api/questions/all` | Get all questions |
| `GET` | `/api/questions/search?keyword=` | Search questions by keyword |
| `GET` | `/api/questions/{id}` | Get a question by ID |
| `PUT` | `/api/questions/approve/{id}` | Approve a question |
| `PUT` | `/api/questions/close/{id}` | Close the thread |
| `DELETE` | `/api/questions/delete/{id}` | Delete a question |

</details>

<details>
<summary><strong>Answers</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/answers/post/{questionId}` | Post an answer |
| `GET` | `/api/answers/question/{questionId}` | Get all answers for a question |
| `PUT` | `/api/answers/approve/{id}` | Approve an answer |
| `DELETE` | `/api/answers/delete/{id}` | Delete an answer |

</details>

<details>
<summary><strong>Likes & Comments</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/likes/toggle/{answerId}` | Like or unlike an answer |
| `GET` | `/api/likes/count/{answerId}` | Get like count |
| `POST` | `/api/comments/add/{answerId}` | Add a comment |
| `GET` | `/api/comments/answer/{answerId}` | Get all comments on an answer |
| `DELETE` | `/api/comments/delete/{id}` | Delete a comment |

</details>

<details>
<summary><strong>Chat</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/chat/send` | Send a message |
| `GET` | `/api/chat/conversation/{id1}/{id2}` | Get conversation between two users |
| `GET` | `/api/chat/unread` | Get unread messages |

</details>

---

### Admin Service — `:8082`

<details>
<summary><strong>Admin Endpoints</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/admin/register` | Register admin |
| `POST` | `/api/admin/login` | Admin login |
| `GET` | `/api/admin/users` | Get all users |
| `PUT` | `/api/admin/users/deactivate/{id}` | Deactivate user |
| `PUT` | `/api/admin/users/activate/{id}` | Activate user |
| `GET` | `/api/admin/questions` | Get all questions |
| `GET` | `/api/admin/questions/unapproved` | Get pending questions |
| `PUT` | `/api/admin/questions/approve/{id}` | Approve question |
| `PUT` | `/api/admin/questions/reject/{id}` | Reject question |
| `PUT` | `/api/admin/questions/close/{id}` | Close thread |
| `DELETE` | `/api/admin/questions/delete/{id}` | Delete question |
| `GET` | `/api/admin/answers/unapproved` | Get pending answers |
| `PUT` | `/api/admin/answers/approve/{id}` | Approve answer |
| `DELETE` | `/api/admin/answers/delete/{id}` | Delete answer |

</details>

---

### Notification Service — `:8083`

<details>
<summary><strong>Notification Endpoints</strong></summary>

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/notifications/send-email` | Send an email |
| `POST` | `/api/notifications/question-asked` | Trigger notification for new question |
| `POST` | `/api/notifications/answer-posted` | Trigger notification for new answer |
| `GET` | `/api/notifications/logs` | View all email logs |
| `GET` | `/api/notifications/logs/failed` | View failed email logs |

</details>

---

## 🔐 Security

- All endpoints except `/login` and `/register` require a valid **JWT Bearer token**
- Tokens expire after **24 hours**
- Admin and User roles use **separate token namespaces**
- Passwords are stored with **BCrypt** hashing

---

## 📧 Email Notifications

The admin receives automated email alerts when:

- A new question is submitted
- A new answer is posted

Configured via **Gmail SMTP** using an [App Password](https://support.google.com/accounts/answer/185833).

---

## 👩‍💻 Author

**Priyanka Show**
📧 [priyankashow1111@gmail.com](mailto:priyankashow1111@gmail.com)
🔗 [github.com/priyankashow1111](https://github.com/priyankashow1111)

---

## 📄 License

This project was developed as part of the **Great Learning Capstone Project** and is intended for educational purposes.
