Here is a **professional README** you can directly use for your GitHub repo.
It is written to **look strong for recruiters and portfolio reviewers**.

---

# Pulse — Real-Time Messaging Platform

Pulse is a full-stack real-time messaging platform that enables users to communicate instantly through live WebSocket connections. The application provides secure authentication, live conversation updates, unread message counters, and read receipts with a modern responsive user interface.

## Live Demo

Frontend:
[https://pulse-real-time-messaging.vercel.app](https://pulse-real-time-messaging.vercel.app)

Backend API:
(Hosted on Railway)

Database:
(Hosted on Render)

<img width="1916" height="857" alt="image" src="https://github.com/user-attachments/assets/21bd50bf-bfc2-41b4-88fe-9fc171a6fa7f" />
<img width="1916" height="862" alt="image" src="https://github.com/user-attachments/assets/a8c7572d-a576-454a-a3f0-6602f7d3fa4c" />

---

# Features

### Real-Time Messaging

* Instant message delivery using **WebSockets (STOMP + SockJS)**
* Live conversation updates without page refresh
* Automatic message streaming to active users

### Messaging System

* Direct user-to-user conversations
* Message timestamps
* Read receipts (✓ delivered, ✓✓ read)
* Unread message counters
* Sidebar auto-updates when new messages arrive

### Authentication & Security

* **JWT-based authentication**
* Secure API endpoints using **Spring Security**
* Stateless authentication architecture
* Password hashing using **BCrypt**

### Conversation Management

* Create conversations instantly
* Live sidebar updates when a new chat starts
* Conversation search functionality
* Automatic conversation sorting by latest message

### Modern User Interface

* Clean responsive layout
* Smooth chat experience
* Profile management modal
* User search modal for starting new chats

---

# Tech Stack

## Frontend

* React
* Vite
* Axios
* STOMP.js
* SockJS
* Lucide React Icons

## Backend

* Java
* Spring Boot
* Spring Security
* Spring WebSocket
* JWT Authentication
* Hibernate / JPA

## Database

* PostgreSQL

## Deployment

* Frontend: **Vercel**
* Backend: **Railway**
* Database: **Render PostgreSQL (cloud hosted)**

---

# Architecture

```
Pulse Real-Time Messaging
│
├── Frontend (React + Vite)
│   ├── Chat UI
│   ├── Conversation sidebar
│   ├── WebSocket client
│   └── Authentication UI
│
├── Backend (Spring Boot)
│   ├── REST API
│   ├── WebSocket messaging
│   ├── JWT authentication
│   └── Business logic services
│
└── Database (PostgreSQL)
    ├── Users
    ├── Conversations
    ├── Conversation Participants
    └── Messages
```

---

# Database Schema

Main entities:

**Users**

* id
* name
* email
* password
* created_at

**Conversations**

* id
* is_group
* created_at

**Conversation Participants**

* id
* conversation_id
* user_id
* unread_count

**Messages**

* id
* conversation_id
* sender_id
* content
* sent_at
* is_read

---

# Real-Time Messaging Flow

1. User sends message through WebSocket
2. Backend processes and saves the message
3. Message is broadcast to conversation subscribers
4. Sidebar conversation preview updates instantly
5. Unread counter increases for other participants
6. When conversation opens, messages are marked as read

---

# Running the Project Locally

## 1. Clone the repository

```
git clone https://github.com/YOUR_USERNAME/pulse-real-time-messaging.git
cd pulse-real-time-messaging
```

---

## 2. Backend Setup

Navigate to backend folder:

```
cd real-time-messaging-platform-backend
```

Configure environment variables:

```
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
APP_JWT_SECRET=
APP_CORS_ALLOWED_ORIGIN=http://localhost:5173
```

Run the backend:

```
./mvnw spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## 3. Frontend Setup

Navigate to frontend folder:

```
cd real-time-messaging-platform-frontend
```

Create `.env` file:

```
VITE_API_URL=http://localhost:8080/api
```

Install dependencies:

```
npm install
```

Run frontend:

```
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```

---

# WebSocket Endpoint

```
ws://localhost:8080/ws
```

Message topic:

```
/topic/conversation/{conversationId}
```

---

# Future Improvements

* Online/offline presence indicators
* Typing indicators
* Group conversations
* Message reactions
* File and image sharing
* Push notifications
* Message deletion/editing
* End-to-end encryption

---

# Author

Mohammed Alfarra

Computer Science Graduate — Florida International University
