#!/bin/bash
# Start all Catrimonial AI services
echo "🐱 Starting Catrimonial AI..."
docker compose up -d
echo ""
echo "All services started! Access:"
echo "  • App:        http://localhost"
echo "  • API:        http://localhost:8080/api"
echo "  • AI Service: http://localhost:8000/docs"
echo "  • Grafana:    http://localhost:3000"
