"""
AI Compatibility Engine
Calculates compatibility between two cats using weighted multi-factor scoring.

Factors:
- Breed Compatibility: 40%
- Age Compatibility: 20%
- Health Compatibility: 20%
- Distance: 10%
- Temperament: 10%
"""

import math
import logging
from typing import List, Tuple
from app.models.schemas import (
    CatProfile, CompatibilityFactor, CompatibilityResponse,
    MatchResult, BatchCompatibilityResponse
)

logger = logging.getLogger(__name__)

# Breed compatibility matrix (1.0 = same breed, varies for cross-breeds)
BREED_COMPATIBILITY = {
    ("Persian", "Persian"): 1.0,
    ("Persian", "Himalayan"): 0.9,
    ("Persian", "British Shorthair"): 0.7,
    ("Siamese", "Siamese"): 1.0,
    ("Siamese", "Oriental"): 0.9,
    ("Siamese", "Balinese"): 0.85,
    ("Maine Coon", "Maine Coon"): 1.0,
    ("Maine Coon", "Norwegian Forest"): 0.8,
    ("Bengal", "Bengal"): 1.0,
    ("Bengal", "Savannah"): 0.75,
    ("Ragdoll", "Ragdoll"): 1.0,
    ("Ragdoll", "Birman"): 0.8,
    ("British Shorthair", "British Shorthair"): 1.0,
    ("Scottish Fold", "Scottish Fold"): 0.5,  # Health concerns with fold-fold breeding
    ("Sphynx", "Sphynx"): 1.0,
    ("Abyssinian", "Abyssinian"): 1.0,
    ("Indian Domestic", "Indian Domestic"): 1.0,
    ("Mixed", "Mixed"): 0.85,
}

# Temperament compatibility
TEMPERAMENT_PAIRS = {
    ("playful", "playful"): 0.9,
    ("playful", "energetic"): 0.95,
    ("calm", "calm"): 0.9,
    ("calm", "gentle"): 0.95,
    ("friendly", "friendly"): 0.9,
    ("friendly", "social"): 0.95,
    ("independent", "independent"): 0.7,
    ("playful", "calm"): 0.6,
    ("aggressive", "aggressive"): 0.3,
    ("shy", "aggressive"): 0.2,
}


class CompatibilityEngine:
    """Multi-factor cat compatibility calculator."""

    BREED_WEIGHT = 0.40
    AGE_WEIGHT = 0.20
    HEALTH_WEIGHT = 0.20
    DISTANCE_WEIGHT = 0.10
    TEMPERAMENT_WEIGHT = 0.10

    def calculate(self, cat1: CatProfile, cat2: CatProfile) -> CompatibilityResponse:
        """Calculate overall compatibility between two cats."""
        factors = []

        # 1. Breed Compatibility (40%)
        breed_score, breed_desc = self._calculate_breed_compatibility(cat1, cat2)
        factors.append(CompatibilityFactor(
            factor="Breed Compatibility",
            score=breed_score,
            weight=self.BREED_WEIGHT,
            description=breed_desc,
            positive=breed_score >= 60
        ))

        # 2. Age Compatibility (20%)
        age_score, age_desc = self._calculate_age_compatibility(cat1, cat2)
        factors.append(CompatibilityFactor(
            factor="Age Compatibility",
            score=age_score,
            weight=self.AGE_WEIGHT,
            description=age_desc,
            positive=age_score >= 60
        ))

        # 3. Health Compatibility (20%)
        health_score, health_desc = self._calculate_health_compatibility(cat1, cat2)
        factors.append(CompatibilityFactor(
            factor="Health Status",
            score=health_score,
            weight=self.HEALTH_WEIGHT,
            description=health_desc,
            positive=health_score >= 60
        ))

        # 4. Distance (10%)
        distance_score, distance_desc = self._calculate_distance_score(cat1, cat2)
        factors.append(CompatibilityFactor(
            factor="Distance",
            score=distance_score,
            weight=self.DISTANCE_WEIGHT,
            description=distance_desc,
            positive=distance_score >= 60
        ))

        # 5. Temperament (10%)
        temp_score, temp_desc = self._calculate_temperament_compatibility(cat1, cat2)
        factors.append(CompatibilityFactor(
            factor="Temperament",
            score=temp_score,
            weight=self.TEMPERAMENT_WEIGHT,
            description=temp_desc,
            positive=temp_score >= 60
        ))

        # Calculate weighted overall score
        overall = sum(f.score * f.weight for f in factors)

        # Generate recommendation
        recommendation = self._generate_recommendation(overall, factors)
        reasoning = self._generate_reasoning(cat1, cat2, factors)
        breeding_compatible = overall >= 70 and health_score >= 70

        return CompatibilityResponse(
            overall_score=round(overall, 1),
            factors=factors,
            recommendation=recommendation,
            breeding_compatible=breeding_compatible,
            reasoning=reasoning
        )

    def batch_calculate(self, source: CatProfile, candidates: List[CatProfile], top_k: int = 10) -> BatchCompatibilityResponse:
        """Calculate compatibility with multiple candidates and return top matches."""
        results = []

        for candidate in candidates:
            if candidate.id == source.id:
                continue

            result = self.calculate(source, candidate)
            results.append(MatchResult(
                cat_id=candidate.id,
                cat_name=candidate.name,
                score=result.overall_score,
                factors=result.factors,
                recommendation=result.recommendation
            ))

        # Sort by score descending
        results.sort(key=lambda x: x.score, reverse=True)

        return BatchCompatibilityResponse(
            source_cat_id=source.id,
            matches=results[:top_k],
            total_evaluated=len(results)
        )

    def _calculate_breed_compatibility(self, cat1: CatProfile, cat2: CatProfile) -> Tuple[float, str]:
        """Calculate breed compatibility score."""
        breed1 = cat1.breed.strip()
        breed2 = cat2.breed.strip()

        if breed1 == breed2:
            # Same breed - check for known genetic concerns
            if breed1 == "Scottish Fold":
                return 50.0, "Same breed but fold-fold breeding has health risks"
            return 95.0, f"Same breed ({breed1}) - excellent genetic compatibility"

        # Check compatibility matrix (both directions)
        key1 = (breed1, breed2)
        key2 = (breed2, breed1)
        score = BREED_COMPATIBILITY.get(key1, BREED_COMPATIBILITY.get(key2, 0.6))

        score_pct = score * 100
        if score_pct >= 80:
            desc = f"{breed1} and {breed2} are highly compatible breeds"
        elif score_pct >= 60:
            desc = f"{breed1} and {breed2} have good breed compatibility"
        else:
            desc = f"{breed1} and {breed2} may have some breed differences"

        return score_pct, desc

    def _calculate_age_compatibility(self, cat1: CatProfile, cat2: CatProfile) -> Tuple[float, str]:
        """Calculate age compatibility. Cats within 6 months are ideal."""
        age_diff = abs(cat1.age_months - cat2.age_months)

        if age_diff <= 3:
            score = 95.0
            desc = "Nearly the same age - ideal"
        elif age_diff <= 6:
            score = 85.0
            desc = "Similar age - very compatible"
        elif age_diff <= 12:
            score = 70.0
            desc = "Moderate age difference"
        elif age_diff <= 24:
            score = 50.0
            desc = "Significant age difference"
        else:
            score = max(20.0, 50.0 - (age_diff - 24) * 2)
            desc = "Large age gap - may affect compatibility"

        return score, desc

    def _calculate_health_compatibility(self, cat1: CatProfile, cat2: CatProfile) -> Tuple[float, str]:
        """Calculate health compatibility based on vaccination and health status."""
        score = 100.0
        issues = []

        # Both vaccinated is ideal
        if not cat1.vaccinated:
            score -= 20
            issues.append(f"{cat1.name} is not vaccinated")
        if not cat2.vaccinated:
            score -= 20
            issues.append(f"{cat2.name} is not vaccinated")

        # Health issues
        if cat1.health_issues and cat1.health_issues.strip():
            score -= 15
            issues.append(f"{cat1.name} has health concerns")
        if cat2.health_issues and cat2.health_issues.strip():
            score -= 15
            issues.append(f"{cat2.name} has health concerns")

        score = max(score, 10.0)

        if not issues:
            desc = "Both cats are healthy and vaccinated"
        else:
            desc = "; ".join(issues)

        return score, desc

    def _calculate_distance_score(self, cat1: CatProfile, cat2: CatProfile) -> Tuple[float, str]:
        """Calculate distance score. Same city is ideal."""
        # Same city
        if cat1.city and cat2.city and cat1.city.lower() == cat2.city.lower():
            return 95.0, f"Both in {cat1.city} - very convenient"

        # Same state
        if cat1.state and cat2.state and cat1.state.lower() == cat2.state.lower():
            return 70.0, f"Same state ({cat1.state})"

        # If coordinates available, calculate distance
        if all([cat1.latitude, cat1.longitude, cat2.latitude, cat2.longitude]):
            distance_km = self._haversine(
                cat1.latitude, cat1.longitude,
                cat2.latitude, cat2.longitude
            )
            if distance_km <= 10:
                return 95.0, f"Very close ({distance_km:.0f} km)"
            elif distance_km <= 50:
                return 80.0, f"Nearby ({distance_km:.0f} km)"
            elif distance_km <= 100:
                return 60.0, f"Moderate distance ({distance_km:.0f} km)"
            elif distance_km <= 300:
                return 40.0, f"Far ({distance_km:.0f} km)"
            else:
                return 20.0, f"Very far ({distance_km:.0f} km)"

        return 50.0, "Distance unknown"

    def _calculate_temperament_compatibility(self, cat1: CatProfile, cat2: CatProfile) -> Tuple[float, str]:
        """Calculate temperament compatibility."""
        temp1 = (cat1.temperament or "").lower().strip()
        temp2 = (cat2.temperament or "").lower().strip()

        if not temp1 or not temp2:
            return 60.0, "Temperament information incomplete"

        # Check known pairs
        key1 = (temp1, temp2)
        key2 = (temp2, temp1)
        score = TEMPERAMENT_PAIRS.get(key1, TEMPERAMENT_PAIRS.get(key2, None))

        if score is not None:
            score_pct = score * 100
        else:
            # Default: check for keyword overlap
            words1 = set(temp1.split(","))
            words2 = set(temp2.split(","))
            overlap = words1 & words2
            if overlap:
                score_pct = 75.0
            else:
                score_pct = 55.0

        if score_pct >= 80:
            desc = "Excellent temperament match"
        elif score_pct >= 60:
            desc = "Good personality compatibility"
        else:
            desc = "Different personalities - may need careful introduction"

        return score_pct, desc

    def _generate_recommendation(self, overall: float, factors: List[CompatibilityFactor]) -> str:
        """Generate human-friendly recommendation."""
        if overall >= 90:
            return "Excellent Match! These cats are highly compatible."
        elif overall >= 75:
            return "Great Match! Strong compatibility across most factors."
        elif overall >= 60:
            return "Good Match. Compatible with some areas to consider."
        elif overall >= 45:
            return "Moderate Match. Some compatibility concerns to review."
        else:
            return "Low Compatibility. Significant differences that may cause issues."

    def _generate_reasoning(self, cat1: CatProfile, cat2: CatProfile, factors: List[CompatibilityFactor]) -> str:
        """Generate detailed reasoning text."""
        positives = [f"✔ {f.description}" for f in factors if f.positive]
        negatives = [f"✘ {f.description}" for f in factors if not f.positive]

        parts = [f"{cat1.name} ❤️ {cat2.name}\n"]
        if positives:
            parts.append("Strengths:\n" + "\n".join(positives))
        if negatives:
            parts.append("Considerations:\n" + "\n".join(negatives))

        return "\n\n".join(parts)

    @staticmethod
    def _haversine(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate distance between two coordinates in km."""
        R = 6371  # Earth's radius in km
        dlat = math.radians(lat2 - lat1)
        dlon = math.radians(lon2 - lon1)
        a = (math.sin(dlat / 2) ** 2 +
             math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) *
             math.sin(dlon / 2) ** 2)
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
        return R * c


# Singleton instance
compatibility_engine = CompatibilityEngine()
