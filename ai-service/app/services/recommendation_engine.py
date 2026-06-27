"""
Smart Recommendation Engine
Netflix-style "You may also like" recommendations using content-based filtering.
Uses KNN and similarity scoring based on cat attributes.
"""

import logging
import math
from typing import List
from app.models.schemas import (
    CatProfile, RecommendedCat, RecommendationResponse
)

logger = logging.getLogger(__name__)


class RecommendationEngine:
    """Content-based cat recommendation engine using similarity scoring."""

    # Feature weights for similarity calculation
    WEIGHTS = {
        "breed": 0.30,
        "age": 0.20,
        "temperament": 0.15,
        "location": 0.15,
        "health": 0.10,
        "color": 0.10,
    }

    def get_recommendations(
        self, user_cats: List[CatProfile],
        available_cats: List[CatProfile],
        top_k: int = 10
    ) -> RecommendationResponse:
        """Generate recommendations based on user's cat preferences."""
        if not user_cats:
            # If no cats, recommend popular/diverse cats
            return self._cold_start_recommendations(available_cats, top_k)

        scored_cats = []

        for candidate in available_cats:
            # Skip user's own cats
            if any(c.id == candidate.id for c in user_cats):
                continue

            # Calculate similarity to each of user's cats
            max_score = 0.0
            best_reason = ""
            best_type = "SIMILAR"

            for user_cat in user_cats:
                score, reason, rec_type = self._calculate_similarity(user_cat, candidate)
                if score > max_score:
                    max_score = score
                    best_reason = reason
                    best_type = rec_type

            if max_score > 0:
                scored_cats.append(RecommendedCat(
                    cat_id=candidate.id,
                    cat_name=candidate.name,
                    score=round(max_score, 2),
                    reason=best_reason,
                    recommendation_type=best_type
                ))

        # Sort by score
        scored_cats.sort(key=lambda x: x.score, reverse=True)

        return RecommendationResponse(
            recommendations=scored_cats[:top_k],
            algorithm="content_based_knn"
        )

    def _calculate_similarity(self, user_cat: CatProfile, candidate: CatProfile) -> tuple:
        """Calculate similarity between user's cat and a candidate."""
        total_score = 0.0
        reasons = []

        # 1. Breed similarity
        breed_score = self._breed_similarity(user_cat.breed, candidate.breed)
        total_score += breed_score * self.WEIGHTS["breed"]
        if breed_score > 0.7:
            reasons.append(f"Similar breed ({candidate.breed})")

        # 2. Age similarity
        age_score = self._age_similarity(user_cat.age_months, candidate.age_months)
        total_score += age_score * self.WEIGHTS["age"]
        if age_score > 0.7:
            reasons.append("Similar age")

        # 3. Temperament similarity
        temp_score = self._temperament_similarity(user_cat.temperament, candidate.temperament)
        total_score += temp_score * self.WEIGHTS["temperament"]
        if temp_score > 0.7:
            reasons.append("Compatible temperament")

        # 4. Location proximity
        loc_score = self._location_similarity(user_cat, candidate)
        total_score += loc_score * self.WEIGHTS["location"]
        if loc_score > 0.7:
            reasons.append("Nearby")

        # 5. Health match
        health_score = self._health_similarity(user_cat, candidate)
        total_score += health_score * self.WEIGHTS["health"]

        # 6. Color preference
        color_score = self._color_similarity(user_cat.color, candidate.color)
        total_score += color_score * self.WEIGHTS["color"]

        # Determine recommendation type
        if breed_score > 0.8 and age_score > 0.7:
            rec_type = "COMPATIBILITY"
        elif loc_score > 0.8:
            rec_type = "NEARBY"
        else:
            rec_type = "SIMILAR"

        reason = ", ".join(reasons) if reasons else f"Based on your preferences for {user_cat.name}"

        return total_score * 100, reason, rec_type

    def _breed_similarity(self, breed1: str, breed2: str) -> float:
        """Calculate breed similarity."""
        if breed1.lower() == breed2.lower():
            return 1.0
        # Simple string matching for now
        b1_words = set(breed1.lower().split())
        b2_words = set(breed2.lower().split())
        if b1_words & b2_words:
            return 0.7
        return 0.3

    def _age_similarity(self, age1: int, age2: int) -> float:
        """Calculate age similarity using Gaussian kernel."""
        diff = abs(age1 - age2)
        return math.exp(-(diff ** 2) / (2 * 12 ** 2))  # sigma = 12 months

    def _temperament_similarity(self, temp1: str, temp2: str) -> float:
        """Calculate temperament similarity using keyword overlap."""
        if not temp1 or not temp2:
            return 0.5

        words1 = set(temp1.lower().replace(",", " ").split())
        words2 = set(temp2.lower().replace(",", " ").split())

        if not words1 or not words2:
            return 0.5

        intersection = words1 & words2
        union = words1 | words2

        return len(intersection) / len(union) if union else 0.5

    def _location_similarity(self, cat1: CatProfile, cat2: CatProfile) -> float:
        """Calculate location proximity."""
        if cat1.city and cat2.city and cat1.city.lower() == cat2.city.lower():
            return 1.0
        if cat1.state and cat2.state and cat1.state.lower() == cat2.state.lower():
            return 0.6

        if all([cat1.latitude, cat1.longitude, cat2.latitude, cat2.longitude]):
            dist = self._haversine(cat1.latitude, cat1.longitude, cat2.latitude, cat2.longitude)
            if dist <= 10:
                return 1.0
            elif dist <= 50:
                return 0.8
            elif dist <= 100:
                return 0.5
            return max(0.1, 1.0 - dist / 500)

        return 0.3

    def _health_similarity(self, cat1: CatProfile, cat2: CatProfile) -> float:
        """Health-based similarity."""
        score = 0.5
        if cat1.vaccinated == cat2.vaccinated:
            score += 0.25
        if cat1.neutered == cat2.neutered:
            score += 0.25
        return score

    def _color_similarity(self, color1: str, color2: str) -> float:
        """Color preference similarity."""
        if not color1 or not color2:
            return 0.5
        if color1.lower() == color2.lower():
            return 1.0
        return 0.3

    def _cold_start_recommendations(self, available_cats: List[CatProfile], top_k: int) -> RecommendationResponse:
        """Recommendations for new users without cats."""
        # Return diverse selection
        recommendations = []
        seen_breeds = set()

        for cat in available_cats:
            if cat.breed not in seen_breeds and cat.vaccinated:
                recommendations.append(RecommendedCat(
                    cat_id=cat.id,
                    cat_name=cat.name,
                    score=75.0,
                    reason=f"Popular {cat.breed} in your area",
                    recommendation_type="POPULAR"
                ))
                seen_breeds.add(cat.breed)

            if len(recommendations) >= top_k:
                break

        return RecommendationResponse(
            recommendations=recommendations,
            algorithm="diversity_based"
        )

    @staticmethod
    def _haversine(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate distance in km."""
        R = 6371
        dlat = math.radians(lat2 - lat1)
        dlon = math.radians(lon2 - lon1)
        a = (math.sin(dlat / 2) ** 2 +
             math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) *
             math.sin(dlon / 2) ** 2)
        return R * 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))


# Singleton
recommendation_engine = RecommendationEngine()
