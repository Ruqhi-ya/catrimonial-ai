"""Recommendation Engine API endpoints."""

from fastapi import APIRouter, HTTPException
from app.models.schemas import RecommendationRequest, RecommendationResponse
from app.services.recommendation_engine import recommendation_engine

router = APIRouter()


@router.post("/recommendations", response_model=RecommendationResponse)
async def get_recommendations(request: RecommendationRequest):
    """Get personalized cat recommendations.

    Uses content-based filtering with KNN similarity scoring.
    Considers breed, age, temperament, location, health status, and color.

    Returns Netflix-style "You may also like" recommendations.
    """
    try:
        result = recommendation_engine.get_recommendations(
            user_cats=request.user_cats,
            available_cats=request.available_cats,
            top_k=request.top_k
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Recommendation engine error: {str(e)}")
