# 🚀 Deployment Guide - Catrimonial AI

## Architecture Overview

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Vercel     │    │   Render     │    │   Render     │
│  (Frontend)  │    │  (Backend)   │    │ (AI Service) │
└──────────────┘    └──────────────┘    └──────────────┘
                           │                    │
                    ┌──────┴──────┐     ┌──────┴──────┐
                    │  Railway    │     │ Cloudinary  │
                    │ PostgreSQL  │     │  (Images)   │
                    └─────────────┘     └─────────────┘
```

---

## 1. Database (Railway PostgreSQL)

1. Create a new PostgreSQL instance on [Railway](https://railway.app)
2. Get the connection URL from the dashboard
3. Run the migration script:
```bash
psql $DATABASE_URL < backend/src/main/resources/db/migration/V1__initial_schema.sql
```

---

## 2. Backend (Render)

### Render Setup

1. Connect your GitHub repo to [Render](https://render.com)
2. Create a new **Web Service**
3. Configure:
   - **Root Directory:** `backend`
   - **Build Command:** `./mvnw package -DskipTests -B`
   - **Start Command:** `java -jar target/*.jar`
   - **Docker:** Or use the Dockerfile

### Environment Variables

```
DATABASE_URL=jdbc:postgresql://host:5432/catrimonial
DATABASE_USERNAME=xxx
DATABASE_PASSWORD=xxx
REDIS_HOST=redis-host
REDIS_PORT=6379
JWT_SECRET=your-production-secret-256-bits
AI_SERVICE_URL=https://your-ai-service.onrender.com
CORS_ORIGINS=https://catrimonial.ai,https://www.catrimonial.ai
APP_BASE_URL=https://catrimonial.ai
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_USERNAME=apikey
MAIL_PASSWORD=your-sendgrid-key
CLOUDINARY_CLOUD_NAME=xxx
CLOUDINARY_API_KEY=xxx
CLOUDINARY_API_SECRET=xxx
```

---

## 3. AI Service (Render)

### Render Setup

1. Create another **Web Service** on Render
2. Configure:
   - **Root Directory:** `ai-service`
   - **Docker:** Use the Dockerfile
   - Or **Build Command:** `pip install -r requirements.txt`
   - **Start Command:** `uvicorn app.main:app --host 0.0.0.0 --port $PORT`

### Environment Variables

```
AI_DEBUG=false
AI_REDIS_URL=redis://your-redis:6379
AI_OPENAI_API_KEY=sk-xxx
AI_BACKEND_URL=https://your-backend.onrender.com/api
```

---

## 4. Frontend (Vercel)

### Vercel Setup

1. Import your GitHub repo on [Vercel](https://vercel.com)
2. Configure:
   - **Framework Preset:** Vite
   - **Root Directory:** `frontend`
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`

### Environment Variables

```
VITE_API_BASE_URL=https://your-backend.onrender.com/api
VITE_WS_URL=wss://your-backend.onrender.com/ws
```

---

## 5. Redis (Upstash or Railway)

Use [Upstash Redis](https://upstash.com) for serverless Redis:
- Free tier: 10K commands/day
- Production: Pay-as-you-go

---

## 6. Image Storage (Cloudinary)

1. Create account at [Cloudinary](https://cloudinary.com)
2. Get API credentials from dashboard
3. Configure upload presets for cat images

---

## Docker Deployment (Self-hosted)

### Production with Docker Compose

```bash
# Clone repository
git clone https://github.com/your-org/catrimonial-ai.git
cd catrimonial-ai

# Copy and configure environment
cp .env.example .env
# Edit .env with production values

# Build and start all services
docker compose -f docker-compose.yml up -d --build

# Check status
docker compose ps

# View logs
docker compose logs -f backend
```

### SSL with Let's Encrypt

Add to nginx config for production:
```nginx
server {
    listen 443 ssl http2;
    server_name catrimonial.ai;

    ssl_certificate /etc/letsencrypt/live/catrimonial.ai/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/catrimonial.ai/privkey.pem;

    # ... rest of config
}
```

---

## Health Checks

| Service | Endpoint | Expected |
|---------|----------|----------|
| Backend | `GET /api/actuator/health` | `{"status":"UP"}` |
| AI Service | `GET /health` | `{"status":"healthy"}` |
| Frontend | `GET /` | HTML page |

---

## Monitoring

- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3000 (admin/admin)
- Backend metrics: `/api/actuator/prometheus`
- AI Service metrics: `/metrics`

---

## Scaling Considerations

| Component | Strategy |
|-----------|----------|
| Backend | Horizontal (multiple instances behind LB) |
| AI Service | Horizontal + GPU instances for ML inference |
| Database | Read replicas, connection pooling |
| Redis | Cluster mode for high availability |
| Frontend | CDN (Vercel/CloudFlare) |
