# 🐱 CATRIMONIAL AI

## The Responsible Cat Owner Network

> "Connect, Care & Find the Perfect Companion for Your Cat."

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Docker](https://img.shields.io/badge/docker-ready-blue.svg)]()

---

## 🌟 Overview

Catrimonial AI is an AI-powered ecosystem for responsible cat owners. It goes far beyond simple matchmaking — it's a comprehensive platform that helps cat owners:

- **Find Compatible Companions** — AI-powered compatibility matching
- **Manage Cat Health** — Vaccination tracking, medical history, health alerts
- **Network with Owners** — Real-time messaging, community forums
- **Discover Nearby** — Find cats, vets, pet stores in your area
- **Responsible Breeding** — AI-guided breeding recommendations
- **Lost & Found** — AI-powered missing cat alerts
- **Adoption** — Connect cats with loving homes
- **Community** — Share stories, ask questions, celebrate milestones

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CATRIMONIAL AI                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────────┐    │
│  │   Frontend    │   │   Backend    │   │   AI Service     │    │
│  │  React/Vite  │◄──►│ Spring Boot  │◄──►│  Python/FastAPI  │    │
│  │  TypeScript  │   │    Java 17   │   │  ML/NLP/CV       │    │
│  │  TailwindCSS │   │  PostgreSQL  │   │  TensorFlow      │    │
│  └──────────────┘   └──────────────┘   └──────────────────┘    │
│         │                   │                    │               │
│         │           ┌───────┴───────┐           │               │
│         │           │               │           │               │
│  ┌──────▼──┐   ┌───▼────┐   ┌─────▼────┐  ┌──▼────────┐      │
│  │ Vercel  │   │  Redis │   │PostgreSQL│  │Cloudinary │      │
│  │ (CDN)   │   │ Cache  │   │   (DB)   │  │ (Storage) │      │
│  └─────────┘   └────────┘   └──────────┘  └───────────┘      │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              Infrastructure & Monitoring                   │   │
│  │  Docker │ Nginx │ Prometheus │ Grafana │ GitHub Actions   │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, Vite, TypeScript, TailwindCSS, ShadCN UI, Framer Motion, Zustand |
| Backend | Java 17, Spring Boot 3, Spring Security, JPA/Hibernate, WebSocket |
| AI Service | Python 3.11, FastAPI, Scikit-learn, TensorFlow, OpenCV, LangChain |
| Database | PostgreSQL 15 |
| Cache | Redis 7 |
| Search | Elasticsearch 8 |
| Storage | Cloudinary |
| DevOps | Docker, Docker Compose, GitHub Actions, Nginx, Prometheus, Grafana |

---

## 📁 Project Structure

```
catrimonial-ai/
├── frontend/                # React + Vite + TypeScript
│   ├── src/
│   │   ├── components/      # Reusable UI components
│   │   ├── pages/           # Application pages
│   │   ├── hooks/           # Custom React hooks
│   │   ├── stores/          # Zustand state management
│   │   ├── services/        # API service layer
│   │   ├── types/           # TypeScript types
│   │   ├── utils/           # Utility functions
│   │   └── animations/      # Cat-themed animations
│   ├── public/
│   └── package.json
│
├── backend/                 # Java Spring Boot
│   ├── src/main/java/
│   │   └── com/catrimonial/
│   │       ├── auth/        # Authentication module
│   │       ├── user/        # User management
│   │       ├── cat/         # Cat profiles
│   │       ├── health/      # Health management
│   │       ├── messaging/   # Real-time chat
│   │       ├── community/   # Community features
│   │       ├── discovery/   # Nearby discovery
│   │       ├── lostfound/   # Lost & Found
│   │       ├── adoption/    # Adoption module
│   │       ├── notification/# Notifications
│   │       └── admin/       # Admin panel
│   ├── src/main/resources/
│   └── pom.xml
│
├── ai-service/              # Python FastAPI
│   ├── app/
│   │   ├── api/             # API routes
│   │   ├── services/        # AI services
│   │   ├── models/          # ML models
│   │   ├── utils/           # Utilities
│   │   └── config/          # Configuration
│   ├── requirements.txt
│   └── Dockerfile
│
├── docker/                  # Docker configurations
│   ├── nginx/
│   ├── prometheus/
│   └── grafana/
│
├── docs/                    # Documentation
│   ├── architecture/
│   ├── api/
│   └── deployment/
│
├── scripts/                 # Utility scripts
├── docker-compose.yml
├── .github/workflows/       # CI/CD
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites

- Node.js 18+
- Java 17+
- Python 3.11+
- Docker & Docker Compose
- PostgreSQL 15
- Redis 7

### Development Setup

```bash
# Clone the repository
git clone https://github.com/your-org/catrimonial-ai.git
cd catrimonial-ai

# Start all services with Docker
docker-compose up -d

# Or start individually:

# Frontend
cd frontend && npm install && npm run dev

# Backend
cd backend && ./mvnw spring-boot:run

# AI Service
cd ai-service && pip install -r requirements.txt && uvicorn app.main:app --reload
```

---

## 🔑 Key Features

### 🤖 AI-Powered
- **Compatibility Engine** — Multi-factor matching algorithm
- **NLP Search** — Natural language cat search
- **Photo Analysis** — Breed detection, health assessment
- **Health Assistant** — RAG-powered veterinary Q&A
- **Smart Recommendations** — Netflix-style cat suggestions
- **Breeding Advisor** — Responsible breeding guidelines

### 🔒 Security
- JWT authentication with refresh tokens
- Role-based access control (User, Admin, Veterinarian)
- Input validation and output sanitization
- Rate limiting and CSRF protection
- Secure file upload with virus scanning
- Comprehensive audit logging

### 📱 Mobile-First Design
- Responsive design with bottom navigation
- Swipe interactions and pull-to-refresh
- Cat-themed animations and micro-interactions
- Glassmorphism-inspired cards
- 60fps animations with Framer Motion

---

## 📄 License

This project is licensed under the MIT License.

---

## 🐾 Made with love for cats everywhere
