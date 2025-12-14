
# 📱 ConvoSphere Backend

**ConvoSphere** is a **scalable, mobile-first chat application backend** with a roadmap to include **agentic AI features** such as smart reminders, conversation summaries, and collaborative meeting scheduling.

This repository contains the **Spring Boot backend (Phase 1)** designed to support a **React Native mobile application**.

---

## 🚀 Project Vision

ConvoSphere aims to be more than just a chat app.

### Phase-wise vision
- **Phase 1** – Core real-time chat (current scope)
- **Phase 2** – “Remind Me” agent (follow-ups & nudges)
- **Phase 3** – “While You Were Away” AI summaries
- **Phase 4** – Agent-assisted group meeting scheduling

---

## 🧱 Phase 1 – Scope

### ✅ Included
- User authentication (JWT-based)
- 1:1 and group conversations
- Real-time messaging (WebSockets + STOMP)
- Media messages (images/videos/GIFs)
- Message history with pagination
- MongoDB-based scalable schema
- Group membership & roles
- Unread message tracking

### ❌ Excluded (Future Phases)
- AI agents
- Push notifications
- Typing indicators
- Message reactions
- End-to-end encryption

---

## 🏗️ Tech Stack

### Backend
- **Java 25**
- **Spring Boot 4**
- Spring Security
- Spring WebSocket (STOMP)
- Spring Data MongoDB
- JWT (Auth0 / Nimbus)
- Lombok
- Spring Validation
- Springdoc OpenAPI (Swagger)
- AWS S3 / MinIO (media storage)

### Database
- **MongoDB**
    - Document-based design
    - Embedded conversation members
    - Separate messages collection for high write throughput

### Client (separate repository)
- React Native
- TypeScript
- WebSockets + REST APIs

---

## 🗂️ High-Level Architecture

```
React Native App
      |
      | REST + WebSocket (JWT)
      v
Spring Boot Backend
      |
      | MongoDB
      |
      | Object Storage (S3 / MinIO)
```

---

## 📦 Data Model Overview (MongoDB)

### Collections
- `users`
- `conversations`
- `messages`
- (optional) `messageReceipts`

### Design Principles
- No DBRefs / `@DocumentReference` for hot-path data
- Embedded members inside conversations
- Manual ID references between collections
- Index-driven query patterns

---

## 🧩 Entity Overview

### User
```
users
- id
- email (unique)
- username (unique)
- passwordHash
- displayName
- avatarUrl
- createdAt
- updatedAt
```

### Conversation
```
conversations
- id
- type (DIRECT | GROUP)
- title
- createdBy
- members[]
- lastMessage (denormalized preview)
- isArchived
- createdAt
- updatedAt
```

### Message
```
messages
- id
- conversationId
- senderId
- content
- messageType (TEXT | IMAGE | VIDEO | GIF | SYSTEM)
- mediaUrl
- replyToId
- createdAt
- editedAt
- isDeleted
```

---

## 🔐 Authentication

- JWT-based authentication
- Passwords hashed using **BCrypt**
- JWT required for:
    - All REST APIs
    - WebSocket connections

---

## 🔄 Real-Time Messaging

- WebSockets with STOMP protocol
- Clients subscribe to:
  ```
  /topic/conversations/{conversationId}
  ```
- Messages are sent to:
  ```
  /app/messages.send
  ```

---

## 📁 Project Structure

```
src/main/java/com/example/convospherebackend/
├── auth/
├── config/
├── controllers/
├── entities/
├── enums/
├── repositories/
├── services/
└── dto/
```

---

## ⚙️ Configuration

### MongoDB
```
spring.data.mongodb.uri=mongodb://localhost:27017/convosphere
spring.data.mongodb.auto-index-creation=true
```

### JWT
```
jwt.secret=your-secret-key
jwt.expiration=86400000
```

---

## 🧪 API Documentation

Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

---

## 🛠️ Development Setup

### Prerequisites
- Java 25
- MongoDB
- Maven
- Docker (optional)

### Run locally
```
mvn spring-boot:run
```

---

## 📈 Scalability Notes

- Messages indexed by `(conversationId, createdAt DESC)`
- Designed for high-write workloads
- Shard-friendly MongoDB schema
- No cross-collection joins in hot paths

---

## 🧠 Future Roadmap

### Phase 2 – Remind Me
- Natural language follow-ups
- Time-based reminders
- Chat resurfacing

### Phase 3 – While You Were Away
- Priority-based summaries
- AI-generated highlights
- User-configurable importance rules

### Phase 4 – Scheduling Agent
- Group availability analysis
- Calendar integration
- Consensus-based scheduling


