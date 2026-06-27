"""Compatibility API endpoints."""

from fastapi import APIRouter, HTTPException
from app.models.schemas import (
    CompatibilityRequest, CompatibilityResponse,
    BatchCompatibilityRequest, BatchCompatibilityResponse
)
from app.services.compatibility_engine import compatibility_engine

router = APIRouter()


@router.post("/compatibility", response_model=CompatibilityResponse)
async def calculate_compatibility(request: CompatibilityRequest):
    """Calculate compatibility between two cats."""
    try:
        result = compatibility_engine.calculate(request.cat1, request.cat2)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Compatibility calculation failed: {str(e)}")


@router.post("/compatibility/batch", response_model=BatchCompatibilityResponse)
async def batch_compatibility(request: BatchCompatibilityRequest):
    """Find top compatible cats from a list of candidates."""
    try:
        result = compatibility_engine.batch_calculate(
            request.source_cat, request.candidate_cats, request.top_k
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Batch compatibility failed: {str(e)}")
