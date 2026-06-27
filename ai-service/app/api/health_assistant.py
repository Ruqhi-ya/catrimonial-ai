"""Health Assistant API endpoints."""

from fastapi import APIRouter, HTTPException
from app.models.schemas import HealthQuestion, HealthResponse
from app.services.health_assistant import health_assistant

router = APIRouter()


@router.post("/health/ask", response_model=HealthResponse)
async def ask_health_question(request: HealthQuestion):
    """Ask the AI health assistant a question about your cat's health.

    Uses RAG architecture with a veterinary knowledge base.
    Includes safety guardrails and always recommends professional consultation.

    Example questions:
    - "My cat is sleeping all day and not eating"
    - "My cat has been vomiting for 2 days"
    - "Why is my cat scratching excessively?"
    """
    try:
        result = health_assistant.answer_question(
            question=request.question,
            cat_info=request.cat_info
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Health assistant error: {str(e)}")
