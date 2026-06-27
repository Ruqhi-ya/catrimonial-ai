"""
Catrimonial AI Service - FastAPI Application
AI-powered compatibility matching, NLP search, photo analysis,
health assistant, and recommendation engine.
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from prometheus_client import make_asgi_app
import logging

from app.config.settings import get_settings
from app.api import compatibility, search, photo_analysis, health_assistant, recommendations, breeding

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)
logger = logging.getLogger(__name__)

settings = get_settings()

# Create FastAPI app
app = FastAPI(
    title=settings.app_name,
    version=settings.app_version,
    description="AI microservice for Catrimonial - The Responsible Cat Owner Network",
    docs_url="/docs",
    redoc_url="/redoc",
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Prometheus metrics endpoint
metrics_app = make_asgi_app()
app.mount("/metrics", metrics_app)

# Include API routers
app.include_router(compatibility.router, prefix="/api/ai", tags=["Compatibility"])
app.include_router(search.router, prefix="/api/ai", tags=["NLP Search"])
app.include_router(photo_analysis.router, prefix="/api/ai", tags=["Photo Analysis"])
app.include_router(health_assistant.router, prefix="/api/ai", tags=["Health Assistant"])
app.include_router(recommendations.router, prefix="/api/ai", tags=["Recommendations"])
app.include_router(breeding.router, prefix="/api/ai", tags=["Breeding Assessment"])


@app.get("/health")
async def health_check():
    """Health check endpoint."""
    return {"status": "healthy", "service": settings.app_name, "version": settings.app_version}


@app.get("/")
async def root():
    """Root endpoint."""
    return {
        "service": "Catrimonial AI Service",
        "version": settings.app_version,
        "endpoints": {
            "compatibility": "/api/ai/compatibility",
            "search": "/api/ai/search",
            "photo-analysis": "/api/ai/photo/analyze",
            "health-assistant": "/api/ai/health/ask",
            "recommendations": "/api/ai/recommendations",
            "breeding": "/api/ai/breeding/assess",
        }
    }


@app.exception_handler(Exception)
async def global_exception_handler(request, exc):
    logger.error(f"Unhandled exception: {exc}", exc_info=True)
    return JSONResponse(
        status_code=500,
        content={"error": "Internal server error", "detail": str(exc) if settings.debug else "An unexpected error occurred"}
    )


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host=settings.host, port=settings.port, reload=settings.debug)
