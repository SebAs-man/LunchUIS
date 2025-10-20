# 🚀 Identity Service

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The Identity Service is the security cornerstone of the **LunchUIS** platform. It handles all aspects of user management, authentication, and authorization using a stateless JWT-based approach.

## ✨ Key Features

-   **Stateless JWT Authentication:** Secure communication using JSON Web Tokens.
-   **Secure Password Hashing:** Utilizes `BCrypt` to store user passwords securely.
-   **Role-Based Access Control (RBAC):** Differentiates between user roles (e.g., `STUDENT`, `ADMIN`).
-   **Clean Architecture:** Built following Clean/Hexagonal Architecture principles, separating domain, application, and infrastructure layers.
-   **API Documentation:** Self-documented endpoints using Swagger (OpenAPI).

## 🔑 API Endpoints

Here are the primary public endpoints provided by this service:

| Method | Path                | Description                                     | Success Response |
| :----- | :------------------ | :---------------------------------------------- | :--------------- |
| `POST` | `/api/auth/signup`  | Registers a new user with the default `STUDENT` role. | `201 Created`    |
| `POST` | `/api/auth/login`   | Authenticates a user and returns a JWT.         | `200 OK`         |

---

## 🏗️ Architecture & Diagrams

> **Note:** For the general package structure and overall system architecture, please refer to the main `README.md` in the project root.

### Class Diagram (Conceptual)

This diagram shows the key classes and their relationships across the main architectural layers.
