"""NLP Search API endpoints."""

from fastapi import APIRouter, HTTPException
from app.models.schemas import NLPSearchRequest, NLPSearchResponse
from app.services.nlp_search import nlp_search_service

router = APIRouter()


@router.post("/search/parse", response_model=NLPSearchResponse)
async def parse_search_query(request: NLPSearchRequest):
    """Parse a natural language query into structured search filters.

    Examples:
    - "White Persian Female near Bangalore"
    - "young male Bengal kitten vaccinated"
    - "friendly calico cat in Mumbai"
    """
    try:
        result = nlp_search_service.parse_query(request.query)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Search parsing failed: {str(e)}")
