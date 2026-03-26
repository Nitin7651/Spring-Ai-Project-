# 🤖 Spring AI Chat — Gemini 2.5 Flash

A Spring Boot application that exposes REST APIs for conversational AI using **Google Gemini 2.5 Flash** via **Spring AI**. Supports both blocking and streaming responses, with **per-user persistent chat memory** backed by **PostgreSQL** via `JdbcChatMemoryRepository`.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.2 |
| AI Integration | Spring AI 2.0.0-M2 |
| AI Model | Google Gemini 2.5 Flash |
| Language | Java 21 |
| Build Tool | Maven |
| Database | PostgreSQL |
| Chat Memory | `JdbcChatMemoryRepository` + `MessageWindowChatMemory` (last 10 messages per user) |

---

## 📁 Project Structure

```
src/main/java/com/ollama/demo/
├── config/
│   └── ChatConfig.java          # ChatClient + JdbcChatMemory beans
├── controller/
│   └── Chatcontroller.java      # REST endpoints (/chat, /stream-chat)
├── service/
│   ├── ChatService.java         # Interface
│   └── ChatServiceImpl.java     # Business logic + PromptChatMemoryAdvisor
└── DemoApplication.java

src/main/resources/
├── application.properties       # Gemini API + PostgreSQL config
└── prompts/
    ├── system-prompt.st         # System-level AI persona prompt
    └── user-prompt.st           # StringTemplate user prompt (topic + query)
```

---

## ⚙️ Configuration

### 1. Get a Gemini API Key

Go to [https://aistudio.google.com/app/apikey](https://aistudio.google.com/app/apikey) and create a free API key.

### 2. Set the Environment Variable

```bash
# Windows (PowerShell)
$env:GEMINI_API_KEY = "your-api-key-here"

# Linux / macOS
export GEMINI_API_KEY=your-api-key-here
```

### 3. Set Up PostgreSQL

Create a database named `springAI` in your local PostgreSQL instance:

```sql
CREATE DATABASE "springAI";
```

> The `spring_ai_chat_memory` table is created automatically on startup — no manual migration needed.

### 4. `application.properties`

```properties
spring.application.name=demo

# Gemini AI
spring.ai.google.genai.api-key=${GEMINI_API_KEY}
spring.ai.google.genai.chat.options.model=gemini-2.5-flash
spring.ai.google.genai.chat.options.temperature=0.2
spring.ai.google.genai.chat.options.max-tokens=200

# PostgreSQL Datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/springAI
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# Auto-create Spring AI chat memory table
spring.ai.chat.memory.repository.jdbc.initialize-schema=ALWAYS
```

---

## ▶️ Running the Application

```bash
cd springai
mvn spring-boot:run
```

Server starts on **`http://localhost:8080`**

---

## 🌐 API Endpoints

Both endpoints require a **`userId`** request header. This value is used as the conversation ID — each user gets their own isolated, **persistent** memory (last 10 messages, stored in PostgreSQL).

---

### `GET /chat`

Blocking — returns the full AI response as a single string.

**Request**
```
GET http://localhost:8080/chat?q=What is Java?
Header: userId: user-alice
```

**Response**
```
Java is a high-level, object-oriented programming language...
```

---

### `GET /stream-chat`

Streaming — returns the response as **Server-Sent Events (SSE)**, tokens arrive in real-time.

**Request**
```
GET http://localhost:8080/stream-chat?q=Explain Spring Boot
Header: userId: user-alice
```

**Response** *(streamed)*
```
Spring Boot is a framework...
```

> **Note:** Use a browser, `curl`, or set Postman to **SSE mode** to view streamed responses properly.

---

## 🧠 Per-User Persistent Chat Memory

Each request must include a `userId` header. The app uses this as a unique conversation key — each user has their own memory of the last **10 messages**, persisted in PostgreSQL. Memory **survives application restarts**.

```
# User Alice introduces herself
GET /chat?q=My name is Alice   →   Header: userId: user-alice

# Alice asks a follow-up — AI remembers (even after restart)
GET /chat?q=What is my name?  →   Header: userId: user-alice
# Response: "Your name is Alice."

# User Bob has separate, isolated memory
GET /chat?q=What is my name?  →   Header: userId: user-bob
# Response: "I don't know your name yet."
```

### How It Works

| Component | Role |
|---|---|
| `JdbcChatMemoryRepository` | Persists messages to PostgreSQL (auto-configured) |
| `MessageWindowChatMemory` | Limits history to the last 10 messages per user |
| `PromptChatMemoryAdvisor` | Injects conversation history into each prompt at request time |
| `userId` header | Used as `conversationId` to route memory per user |

---

## 🧪 Testing with Postman

1. Set method to `GET`
2. Add query param `q` = your question
3. Add header `userId` = any unique string per user
4. For `/stream-chat`, click **Send** and check the **Response → Body** tab for SSE events

---

## 📦 Key Dependencies (`pom.xml`)

| Dependency | Purpose |
|---|---|
| `spring-ai-starter-model-google-genai` | Gemini AI model integration |
| `spring-ai-starter-model-chat-memory-repository-jdbc` | JDBC-backed chat memory |
| `spring-boot-starter-jdbc` | Spring JDBC support |
| `postgresql` | PostgreSQL driver |

---

## 📄 License

This project is for demo/learning purposes.
