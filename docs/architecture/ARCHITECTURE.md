# 🏗️ Catrimonial AI - System Architecture

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT LAYER                                    │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    React SPA (Vite + TypeScript)                      │    │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │    │
│  │  │   Auth   │ │Dashboard │ │Cat Mgmt  │ │Messaging │ │Community │ │    │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘ │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ HTTPS / WebSocket
┌──────────────────────────────────▼──────────────────────────────────────────┐
│                              GATEWAY LAYER                                   │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                      Nginx Reverse Proxy                              │    │
│  │           (SSL Termination, Rate Limiting, Load Balancing)            │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└──────────────────┬─────────────────────────────────┬────────────────────────┘
                   │                                 │
┌──────────────────▼──────────────────┐  ┌──────────▼────────────────────────┐
│         APPLICATION LAYER           │  │         AI SERVICE LAYER           │
│  ┌──────────────────────────────┐   │  │  ┌──────────────────────────────┐ │
│  │     Spring Boot Application  │   │  │  │     FastAPI Application      │ │
│  │                              │   │  │  │                              │ │
│  │  ┌────────┐  ┌───────────┐  │   │  │  │  ┌──────────────────────┐   │ │
│  │  │  Auth  │  │   REST    │  │   │  │  │  │ Compatibility Engine │   │ │
│  │  │Module  │  │Controllers│  │   │  │  │  │ NLP Search           │   │ │
│  │  └────────┘  └───────────┘  │   │  │  │  │ Photo Analysis       │   │ │
│  │  ┌────────┐  ┌───────────┐  │   │  │  │  │ Health Assistant     │   │ │
│  │  │WebSocket│  │  Service  │  │   │  │  │  │ Recommendations      │   │ │
│  │  │ Server │  │   Layer   │  │   │  │  │  │ Breeding Advisor     │   │ │
│  │  └────────┘  └───────────┘  │   │  │  │  └──────────────────────┘   │ │
│  │  ┌────────┐  ┌───────────┐  │   │  │  │                              │ │
│  │  │Security│  │Repository │  │   │  │  │  ┌──────────────────────┐   │ │
│  │  │Filters │  │   Layer   │  │   │  │  │  │   ML Models          │   │ │
│  │  └────────┘  └───────────┘  │   │  │  │  │   TensorFlow/SKLearn │   │ │
│  └──────────────────────────────┘   │  │  │  └──────────────────────┘   │ │
└──────────────┬──────────────────────┘  │  └──────────────────────────────┘ │
               │                         └───────────────────────────────────┘
┌──────────────▼──────────────────────────────────────────────────────────────┐
│                              DATA LAYER                                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  PostgreSQL  │  │    Redis     │  │Elasticsearch │  │ Cloudinary   │   │
│  │   (Primary)  │  │   (Cache)    │  │   (Search)   │  │  (Storage)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          MONITORING LAYER                                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                     │
│  │  Prometheus  │  │   Grafana    │  │   Logging    │                     │
│  │  (Metrics)   │  │ (Dashboards) │  │   (ELK/Loki)│                     │
│  └──────────────┘  └──────────────┘  └──────────────┘                     │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Module Architecture

### Authentication Flow
```
Client → Nginx → Spring Security Filter → JWT Validation → Controller → Service → Repository → DB
                                        ↓ (if invalid)
                                   401 Unauthorized
```

### AI Matching Flow
```
User Request → Backend API → AI Service (FastAPI)
                                    ↓
                            ┌───────────────┐
                            │ Compatibility  │
                            │   Calculator   │
                            ├───────────────┤
                            │ Breed: 40%    │
                            │ Age: 20%      │
                            │ Health: 20%   │
                            │ Distance: 10% │
                            │ Temper: 10%   │
                            └───────┬───────┘
                                    ↓
                            Scored Results → Backend → Client
```

### Real-time Messaging
```
Client A ←→ WebSocket ←→ Spring WebSocket Handler ←→ Message Broker ←→ Client B
                              ↓
                         PostgreSQL (persistence)
                              ↓
                         Redis (presence/status)
```

## Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Frontend Framework | React + Vite | Fast HMR, excellent TypeScript support, large ecosystem |
| State Management | Zustand | Lightweight, no boilerplate, TypeScript-first |
| Backend Framework | Spring Boot 3 | Enterprise-grade, excellent security, WebSocket support |
| AI Framework | FastAPI | Async, fast, excellent for ML serving, auto-docs |
| Database | PostgreSQL | ACID compliance, JSON support, spatial queries |
| Cache | Redis | Sub-ms latency, pub/sub for real-time, session store |
| Search | Elasticsearch | Full-text search, vector similarity, geo queries |
| Auth | JWT + Refresh Tokens | Stateless, scalable, secure rotation |
| Storage | Cloudinary | CDN, image transformations, AI tagging |
| Deployment | Docker + K8s | Portable, scalable, consistent environments |

## Security Architecture

```
┌────────────────────────────────────────────────┐
│              Security Layers                    │
├────────────────────────────────────────────────┤
│ L1: Network    │ HTTPS, Firewall, Rate Limit  │
│ L2: Gateway    │ Nginx, CORS, Headers         │
│ L3: Auth       │ JWT, Refresh Tokens, MFA     │
│ L4: AuthZ      │ RBAC (User, Admin, Vet)      │
│ L5: Input      │ Validation, Sanitization     │
│ L6: Data       │ Encryption at rest/transit   │
│ L7: Audit      │ Logging, Monitoring, Alerts  │
└────────────────────────────────────────────────┘
```

## Data Flow

### Cat Profile Creation
1. User uploads cat photos → Cloudinary (with moderation)
2. AI Service analyzes photos → Breed detection, health assessment
3. Backend validates and stores cat profile → PostgreSQL
4. Elasticsearch indexes cat for search
5. AI generates initial compatibility scores
6. Nearby users receive notifications

### AI Health Query
1. User asks health question
2. Backend forwards to AI Service
3. LangChain RAG pipeline:
   - Embed query with Sentence Transformers
   - Search vector store for relevant context
   - Generate response with guardrails
4. Response returned with citations and disclaimers
