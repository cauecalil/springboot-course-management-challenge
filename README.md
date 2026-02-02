# Spring Boot Technical Study: Course Management API

This repository was created to document my progress learning **Java** and building RESTful APIs using the **Spring Boot** ecosystem. As a developer with experience in other stacks, this project aims to strengthen my understanding of backend architecture, CRUD operations, and API design patterns.

---

## 📋 The Challenge: Business Requirements

The project consists of developing a fictitious API for a **programming courses company**, where the main goal is to manage courses using standard CRUD operations.

---

## 1. Domain Entity

The system must be structured around a single main entity:

### Course

- `id` – Unique identifier of the course
- `name` – Course name
- `category` – Course category
- `active` – Indicates whether the course is active or not
- `created_at` – Timestamp when the course was created
- `updated_at` – Timestamp when the course was last updated

---

## 2. Functional Requirements

### Create Course
- Create a new course by sending `name`, `category`, and `professor` in the request body.
- The fields `id`, `created_at`, and `updated_at` must be generated automatically.

### List Courses
- List all courses stored in the database.
- Allow filtering by:
    - `name`
    - `category`

### Update Course (by ID)
- Update a course using its `id`.
- The request body must contain **only one** of the following:
    - `name`
    - `category`
    - `professor`
- If only `name` is provided, `category` must not be updated and vice-versa.
- If `active` is sent in this request, it must be ignored.

### Delete Course
- Remove a course using its `id`.

### Toggle Course Active Status
- Change the course status between `true` and `false`.

---

## 🌐 API Routes

| Method | Endpoint                | Description                     |
|--------|-------------------------|---------------------------------|
| POST   | `/courses`              | Create a new course             |
| GET    | `/courses`              | List all courses (with filters) |
| PUT    | `/courses/{id}`         | Update course                   |
| DELETE | `/courses/{id}`         | Delete course                   |
| PATCH  | `/courses/{id}/active`  | Toggle active status            |

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **ORM:** Hibernate / JPA
- **Database:** PostgreSQL
- **IDE:** IntelliJ IDEA

---

*This project is intended for learning purposes and portfolio demonstration.*
