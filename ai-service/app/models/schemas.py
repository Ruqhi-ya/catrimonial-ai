"""Pydantic models for AI service request/response schemas."""

from pydantic import BaseModel, Field
from typing import Optional, List
from enum import Enum


class Gender(str, Enum):
    MALE = "MALE"
    FEMALE = "FEMALE"


# ====== Compatibility Models ======

class CatProfile(BaseModel):
    """Cat profile for compatibility calculation."""
    id: str
    name: str
    breed: str
    gender: Gender
    age_months: int = Field(ge=1)
    weight_kg: Optional[float] = None
    color: Optional[str] = None
    vaccinated: bool = False
    neutered: bool = False
    temperament: Optional[str] = None
    health_issues: Optional[str] = None
    city: Optional[str] = None
    state: Optional[str] = None
    latitude: Optional[float] = None
    longitude: Optional[float] = None


class CompatibilityRequest(BaseModel):
    """Request to calculate compatibility between two cats."""
    cat1: CatProfile
    cat2: CatProfile


class CompatibilityFactor(BaseModel):
    """Individual compatibility factor."""
    factor: str
    score: float = Field(ge=0, le=100)
    weight: float
    description: str
    positive: bool


class CompatibilityResponse(BaseModel):
    """Compatibility calculation response."""
    overall_score: float = Field(ge=0, le=100)
    factors: List[CompatibilityFactor]
    recommendation: str
    breeding_compatible: bool
    reasoning: str


class BatchCompatibilityRequest(BaseModel):
    """Request to find compatible cats for a given cat."""
    source_cat: CatProfile
    candidate_cats: List[CatProfile]
    top_k: int = Field(default=10, ge=1, le=50)


class MatchResult(BaseModel):
    """Single match result."""
    cat_id: str
    cat_name: str
    score: float
    factors: List[CompatibilityFactor]
    recommendation: str


class BatchCompatibilityResponse(BaseModel):
    """Batch compatibility response."""
    source_cat_id: str
    matches: List[MatchResult]
    total_evaluated: int


# ====== NLP Search Models ======

class NLPSearchRequest(BaseModel):
    """Natural language search request."""
    query: str = Field(min_length=2, max_length=500)


class ParsedSearchFilters(BaseModel):
    """Parsed filters from NLP query."""
    breed: Optional[str] = None
    gender: Optional[str] = None
    color: Optional[str] = None
    city: Optional[str] = None
    state: Optional[str] = None
    age_min_months: Optional[int] = None
    age_max_months: Optional[int] = None
    vaccinated: Optional[bool] = None
    neutered: Optional[bool] = None
    temperament: Optional[str] = None
    original_query: str


class NLPSearchResponse(BaseModel):
    """NLP search response."""
    filters: ParsedSearchFilters
    confidence: float
    interpreted_as: str


# ====== Photo Analysis Models ======

class PhotoAnalysisResponse(BaseModel):
    """Photo analysis results."""
    detected_breed: str
    breed_confidence: float
    estimated_age_range: str
    body_condition: str
    body_condition_score: float  # 1-9 scale
    possible_health_concerns: List[str]
    is_cat: bool
    image_quality: str


class DuplicateCheckResponse(BaseModel):
    """Duplicate image detection result."""
    is_duplicate: bool
    similarity_score: float
    similar_cat_ids: List[str]


# ====== Health Assistant Models ======

class HealthQuestion(BaseModel):
    """Health assistant question."""
    question: str = Field(min_length=5, max_length=1000)
    cat_info: Optional[CatProfile] = None
    conversation_history: Optional[List[dict]] = None


class HealthResponse(BaseModel):
    """Health assistant response."""
    answer: str
    possible_causes: List[str]
    suggestions: List[str]
    urgency_level: str  # LOW, MEDIUM, HIGH, EMERGENCY
    visit_vet: bool
    disclaimer: str


# ====== Recommendation Models ======

class RecommendationRequest(BaseModel):
    """Recommendation request."""
    user_id: str
    user_cats: List[CatProfile]
    available_cats: List[CatProfile]
    top_k: int = Field(default=10, ge=1, le=50)


class RecommendedCat(BaseModel):
    """Single recommendation."""
    cat_id: str
    cat_name: str
    score: float
    reason: str
    recommendation_type: str  # COMPATIBILITY, SIMILAR, POPULAR, NEARBY


class RecommendationResponse(BaseModel):
    """Recommendation response."""
    recommendations: List[RecommendedCat]
    algorithm: str


# ====== Breeding Assessment Models ======

class BreedingAssessmentRequest(BaseModel):
    """Breeding assessment request."""
    cat: CatProfile
    vaccination_count: int = 0
    last_health_check_days_ago: Optional[int] = None


class BreedingAssessmentResponse(BaseModel):
    """Breeding assessment result."""
    is_eligible: bool
    overall_score: float
    reasons: List[dict]
    recommendation: str
    age_eligible: bool
    weight_eligible: bool
    vaccination_complete: bool
    no_health_issues: bool
    minimum_wait_months: Optional[int] = None
