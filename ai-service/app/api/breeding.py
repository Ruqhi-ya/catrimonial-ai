"""Breeding Assessment API endpoints."""

from fastapi import APIRouter, HTTPException
from app.models.schemas import BreedingAssessmentRequest, BreedingAssessmentResponse
from app.services.breeding_advisor import breeding_advisor

router = APIRouter()


@router.post("/breeding/assess", response_model=BreedingAssessmentResponse)
async def assess_breeding_eligibility(request: BreedingAssessmentRequest):
    """Assess whether a cat is eligible for responsible breeding.

    Evaluates:
    - Age (must be 1-7 years)
    - Weight (minimum thresholds by gender)
    - Vaccination completeness
    - Health status (no known issues)
    - Recent health check

    Returns detailed eligibility report with recommendations.
    """
    try:
        result = breeding_advisor.assess(
            cat=request.cat,
            vaccination_count=request.vaccination_count,
            last_health_check_days_ago=request.last_health_check_days_ago
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Breeding assessment error: {str(e)}")
