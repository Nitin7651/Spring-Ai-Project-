# 🤖 Spring AI Chat — Gemini 2.5 Flash

A Spring Boot application that exposes REST APIs for conversational AI using **Google Gemini 2.5 Flash** via **Spring AI**. Supports both blocking and streaming responses, with **per-user isolated chat memory** managed through a `userId` request header.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.2 |
| AI Integration | Spring AI 2.0.0-M2 |
| AI Model | Google Gemini 2.5 Flash |
| Language | Java 21 |
| Build Tool | Maven |
| Memory | In-Memory (per user, last 10 messages) |

---

## 📁 Project Structure

```
src/main/java/com/ollama/demo/
├── config/
│   └── ChatConfig.java          # ChatClient, memory beans
├── controller/
│   └── Chatcontroller.java      # REST endpoints
├── service/
│   ├── ChatService.java         # Interface
│   └── ChatServiceImpl.java     # Business logic + memory advisor
└── DemoApplication.java

src/main/resources/
├── application.properties       # Gemini API config
└── prompts/
    └── user-prompt.st           # StringTemplate user prompt
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

### 3. `application.properties`

```properties
spring.ai.google.genai.api-key=${GEMINI_API_KEY}
spring.ai.google.genai.chat.options.model=gemini-2.5-flash
spring.ai.google.genai.chat.options.temperature=0.2
spring.ai.google.genai.chat.options.max-tokens=200
```

---

## ▶️ Running the Application

```bash
mvn spring-boot:run
```

Server starts on **`http://localhost:8080`**

---

## 🌐 API Endpoints

Both endpoints require a **`userId`** request header. This value is used as the conversation ID — each user gets their own isolated memory (last 10 messages).

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

## 🧠 Per-User Chat Memory

Each request must include a `userId` header. The app uses this as a unique conversation key — each user has their own memory of the last **10 messages**.

```
# User Alice introduces herself
GET /chat?q=My name is Alice   →   Header: userId: user-alice

# Alice asks a follow-up — AI remembers
GET /chat?q=What is my name?  →   Header: userId: user-alice
# Response: "Your name is Alice."

# User Bob knows nothing about Alice
GET /chat?q=What is my name?  →   Header: userId: user-bob
# Response: "I don't know your name yet."
```

Memory is **in-memory only** — it resets when the application restarts.

---

## 🧪 Testing with Postman

1. Set method to `GET`
2. Add query param `q` = your question
3. Add header `userId` = any unique string per user
4. For `/stream-chat`, click **Send** and check the **Response → Body** tab for SSE events

---

## 📄 License

This project is for demo/learning purposes.
