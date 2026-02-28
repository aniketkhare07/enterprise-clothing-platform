# Enterprise Clothing E-Commerce Platform

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green) ![Angular](https://img.shields.io/badge/Angular-17-red) ![Postgres](https://img.shields.io/badge/PostgreSQL-16-blue)

## 📖 Overview
A production-grade, cloud-native e-commerce microservices platform designed to handle high-traffic loads. This project demonstrates strict adherence to Clean Architecture, 12-Factor App methodology, and FAANG-level best practices.

## 🏗 Architecture
This system utilizes a **Modular Monolith** approach, designed for eventual strangulation into **Microservices**.
- **Backend:** Java 21, Spring Boot 3 (Web, Security, JPA), Flyway Migration.
- **Frontend:** Angular 17+ (Standalone Components, Signals), TailwindCSS.
- **Database:** PostgreSQL 16 (Relational), Redis (Caching - TBD).
- **Messaging:** Apache Kafka (Event-Driven Architecture - TBD).
- **Infrastructure:** Docker Compose, Kubernetes (Future).

## 🚀 Getting Started

### Prerequisites
- Java 21 LTS
- Node.js v20+
- Docker & Docker Compose (or OrbStack)
- Maven 3.9+

### Local Environment Setup

1. **Clone the repository**
   ```bash
   git clone <YOUR_REPO_URL>
   cd enterprise-clothing-platform