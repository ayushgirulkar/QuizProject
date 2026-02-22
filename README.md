# 🤖 AI-QuizApp

AI-QuizApp is an interactive online quiz platform designed for educational purposes. It allows **Admins** to generate quizzes automatically from any paragraph or topic using the **Gemini API**, while **Students** can attempt quizzes using unique test codes, receive instant results, and track their performance history.

The project is built using **Spring Boot**, **PostgreSQL**, **React**, **HTML/CSS**, and **Gemini API (gemini-2.5-flash)**.

---

## ✨ Features

### Admin
- Signup / Login authentication
- Create quiz by pasting paragraph or topic
- Set title, validity date, duration, and number of questions
- AI-based question & answer generation
- Unique test code generation
- View student-wise and test-wise results
- Track student attempt history

### Student
- Signup / Login authentication
- Join quiz using unique test code
- Timed quiz attempt
- Auto-submit on time expiry
- Instant result display
- View quiz and marks history

---

## 🛠 Tech Stack
- Backend: Spring Boot
- Database: PostgreSQL
- Frontend: React, HTML, CSS
- AI Model: Gemini API (gemini-2.5-flash)

---

## ⚙️ Installation & Setup (YAML Format)

```yaml
Setup:
  Prerequisites:
    Backend:
      - Java 17+
      - Maven
      - PostgreSQL
    Frontend:
      - Node.js v16+
      - npm
    AI:
      - Gemini API Key (gemini-2.5-flash)

  Backend:
    Path: backend/
    Configuration:
      application.properties:
        spring.datasource.url: jdbc:postgresql://localhost:5432/quizapp
        spring.datasource.username: your_db_user
        spring.datasource.password: your_db_password
        spring.jpa.hibernate.ddl-auto: update
        gemini.api.key: YOUR_GEMINI_API_KEY
        gemini.model: gemini-2.5-flash
    Commands:
      - cd backend
      - ./mvnw spring-boot:run
    Server:
      URL: http://localhost:8080
    Responsibilities:
      - Admin & Student authentication
      - Quiz generation using Gemini API
      - Test timing and auto submission
      - Result calculation and history tracking
      - PostgreSQL database management

  Frontend:
    Path: frontend/
    Environment:
      REACT_APP_API_BASE_URL: http://localhost:8080
    Commands:
      - cd frontend
      - npm install
      - npm start
    Server:
      URL: http://localhost:3000
    Responsibilities:
      - Admin & Student UI
      - Quiz creation and test code entry
      - Timed quiz interface
      - Auto-submit on time expiry
      - Result display and performance history


---
## 👨‍💻 Developer
Ayush Girulkar
GitHub: https://github.com/ayushgirulkar
