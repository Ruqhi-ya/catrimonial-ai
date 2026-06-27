#!/bin/bash
# ============================================
# Catrimonial AI - Development Setup Script
# ============================================

set -e

echo "🐱 Catrimonial AI - Setup Script"
echo "=================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

print_step() { echo -e "${GREEN}[✓]${NC} $1"; }
print_warn() { echo -e "${YELLOW}[!]${NC} $1"; }
print_error() { echo -e "${RED}[✗]${NC} $1"; }

# Check prerequisites
echo "Checking prerequisites..."
command -v docker >/dev/null 2>&1 || { print_error "Docker is required. Install: https://docs.docker.com/get-docker/"; exit 1; }
command -v docker compose >/dev/null 2>&1 || command -v docker-compose >/dev/null 2>&1 || { print_error "Docker Compose is required."; exit 1; }
print_step "Docker found"

# Create .env if not exists
if [ ! -f .env ]; then
    echo ""
    echo "Creating .env file from template..."
    cp .env.example .env
    print_step ".env created - please update with your credentials"
else
    print_step ".env already exists"
fi

# Start infrastructure services
echo ""
echo "Starting infrastructure services..."
docker compose up -d postgres redis
print_step "PostgreSQL and Redis started"

# Wait for services
echo "Waiting for services to be ready..."
sleep 5

echo ""
echo "============================================"
echo "🎉 Infrastructure is ready!"
echo ""
echo "Services:"
echo "  • PostgreSQL: localhost:5432"
echo "  • Redis:      localhost:6379"
echo ""
echo "To start all services:"
echo "  docker compose up -d"
echo ""
echo "To start individual services for development:"
echo ""
echo "  Backend (Java):  cd backend && ./mvnw spring-boot:run"
echo "  Frontend (React): cd frontend && npm install && npm run dev"
echo "  AI Service (Python): cd ai-service && pip install -r requirements.txt && uvicorn app.main:app --reload"
echo ""
echo "Access:"
echo "  • Frontend:      http://localhost:5173"
echo "  • Backend API:   http://localhost:8080/api"
echo "  • AI Service:    http://localhost:8000/docs"
echo "  • Swagger UI:    http://localhost:8080/api/swagger-ui.html"
echo "  • Grafana:       http://localhost:3000 (admin/admin)"
echo "  • Prometheus:    http://localhost:9090"
echo "============================================"
