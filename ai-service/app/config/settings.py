from pydantic_settings import BaseSettings
from functools import lru_cache


class Settings(BaseSettings):
    """Application configuration via environment variables."""

    # App
    app_name: str = "Catrimonial AI Service"
    app_version: str = "1.0.0"
    debug: bool = False

    # Server
    host: str = "0.0.0.0"
    port: int = 8000

    # Redis
    redis_url: str = "redis://localhost:6379"

    # OpenAI (for health assistant)
    openai_api_key: str = ""
    openai_model: str = "gpt-3.5-turbo"

    # ML Model paths
    breed_model_path: str = "models/breed_classifier.h5"
    embedding_model: str = "all-MiniLM-L6-v2"

    # Backend API
    backend_url: str = "http://localhost:8080/api"

    # Rate limiting
    rate_limit_per_minute: int = 60

    class Config:
        env_file = ".env"
        env_prefix = "AI_"


@lru_cache()
def get_settings() -> Settings:
    return Settings()
