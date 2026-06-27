"""
NLP Search Service
Parses natural language queries into structured search filters.
Example: "White Persian Female near Bangalore" -> {breed: Persian, gender: Female, color: White, city: Bangalore}
"""

import re
import logging
from typing import Optional
from app.models.schemas import ParsedSearchFilters, NLPSearchResponse

logger = logging.getLogger(__name__)

# Known cat breeds for detection
CAT_BREEDS = [
    "Persian", "Siamese", "Maine Coon", "British Shorthair", "Bengal",
    "Ragdoll", "Abyssinian", "Scottish Fold", "Sphynx", "Birman",
    "Oriental", "Russian Blue", "Norwegian Forest", "Devon Rex",
    "Himalayan", "Burmese", "Savannah", "Turkish Angora", "Manx",
    "Indian Domestic", "Mixed", "Domestic Shorthair", "Domestic Longhair",
    "American Shorthair", "Exotic Shorthair", "Bombay", "Tonkinese",
]

# Common cat colors
CAT_COLORS = [
    "white", "black", "orange", "ginger", "tabby", "calico",
    "tortoiseshell", "grey", "gray", "brown", "cream", "silver",
    "golden", "blue", "red", "bicolor", "tricolor", "spotted",
    "striped", "tuxedo",
]

# Gender keywords
GENDER_KEYWORDS = {
    "male": ["male", "boy", "tom", "tomcat", "stud", "he"],
    "female": ["female", "girl", "queen", "she", "molly"],
}

# Age keywords
AGE_PATTERNS = {
    "kitten": (1, 6),
    "young": (3, 12),
    "adult": (12, 84),
    "senior": (84, 300),
    "baby": (1, 4),
}

# Location indicators
LOCATION_INDICATORS = ["near", "in", "from", "at", "around", "nearby", "within"]

# Vaccination/neutered keywords
VACCINATED_KEYWORDS = ["vaccinated", "vaxxed", "immunized", "shots"]
NEUTERED_KEYWORDS = ["neutered", "spayed", "fixed", "sterilized"]

# Temperament keywords
TEMPERAMENT_KEYWORDS = [
    "playful", "calm", "gentle", "friendly", "energetic", "lazy",
    "social", "independent", "affectionate", "active", "quiet",
]


class NLPSearchService:
    """Natural language query parser for cat search."""

    def parse_query(self, query: str) -> NLPSearchResponse:
        """Parse a natural language query into structured filters."""
        query_lower = query.lower().strip()
        original_query = query

        filters = ParsedSearchFilters(original_query=original_query)
        confidence = 0.0
        factors = 0

        # 1. Detect breed
        breed = self._detect_breed(query_lower)
        if breed:
            filters.breed = breed
            confidence += 0.25
            factors += 1

        # 2. Detect gender
        gender = self._detect_gender(query_lower)
        if gender:
            filters.gender = gender
            confidence += 0.2
            factors += 1

        # 3. Detect color
        color = self._detect_color(query_lower)
        if color:
            filters.color = color
            confidence += 0.15
            factors += 1

        # 4. Detect location
        city = self._detect_location(query, query_lower)
        if city:
            filters.city = city
            confidence += 0.2
            factors += 1

        # 5. Detect age range
        age_min, age_max = self._detect_age(query_lower)
        if age_min is not None:
            filters.age_min_months = age_min
            filters.age_max_months = age_max
            confidence += 0.1
            factors += 1

        # 6. Detect vaccination status
        if any(kw in query_lower for kw in VACCINATED_KEYWORDS):
            filters.vaccinated = True
            confidence += 0.05
            factors += 1

        # 7. Detect neutered status
        if any(kw in query_lower for kw in NEUTERED_KEYWORDS):
            filters.neutered = True
            confidence += 0.05
            factors += 1

        # 8. Detect temperament
        temperament = self._detect_temperament(query_lower)
        if temperament:
            filters.temperament = temperament
            confidence += 0.1
            factors += 1

        # Normalize confidence
        confidence = min(confidence, 1.0)
        if factors == 0:
            confidence = 0.1

        # Generate interpretation
        interpreted_as = self._generate_interpretation(filters)

        return NLPSearchResponse(
            filters=filters,
            confidence=round(confidence, 2),
            interpreted_as=interpreted_as
        )

    def _detect_breed(self, query: str) -> Optional[str]:
        """Detect cat breed from query."""
        for breed in CAT_BREEDS:
            if breed.lower() in query:
                return breed
        # Handle multi-word breeds
        if "maine coon" in query:
            return "Maine Coon"
        if "british shorthair" in query:
            return "British Shorthair"
        if "scottish fold" in query:
            return "Scottish Fold"
        if "russian blue" in query:
            return "Russian Blue"
        return None

    def _detect_gender(self, query: str) -> Optional[str]:
        """Detect gender from query."""
        for gender, keywords in GENDER_KEYWORDS.items():
            for kw in keywords:
                if re.search(r'\b' + kw + r'\b', query):
                    return gender.upper()
        return None

    def _detect_color(self, query: str) -> Optional[str]:
        """Detect color from query."""
        for color in CAT_COLORS:
            if re.search(r'\b' + color + r'\b', query):
                return color.capitalize()
        return None

    def _detect_location(self, original_query: str, query_lower: str) -> Optional[str]:
        """Detect city/location from query."""
        # Look for location after indicators
        for indicator in LOCATION_INDICATORS:
            pattern = indicator + r'\s+([A-Z][a-zA-Z\s]+?)(?:\s*$|\s+(?:who|that|with))'
            match = re.search(pattern, original_query)
            if match:
                return match.group(1).strip()

        # Try simpler pattern: indicator + capitalized word
        for indicator in LOCATION_INDICATORS:
            pattern = indicator + r'\s+([A-Z][a-zA-Z]+)'
            match = re.search(pattern, original_query)
            if match:
                return match.group(1).strip()

        # Look for capitalized words that might be city names (not breed/color)
        words = original_query.split()
        for word in words:
            if (word[0].isupper() and
                word.lower() not in [b.lower() for b in CAT_BREEDS] and
                word.lower() not in CAT_COLORS and
                word.lower() not in GENDER_KEYWORDS["male"] + GENDER_KEYWORDS["female"] and
                len(word) > 2):
                return word

        return None

    def _detect_age(self, query: str) -> tuple:
        """Detect age range from query."""
        for keyword, (min_months, max_months) in AGE_PATTERNS.items():
            if keyword in query:
                return min_months, max_months

        # Try to find numeric age
        match = re.search(r'(\d+)\s*(?:month|mo)', query)
        if match:
            months = int(match.group(1))
            return max(1, months - 2), months + 2

        match = re.search(r'(\d+)\s*(?:year|yr)', query)
        if match:
            years = int(match.group(1))
            months = years * 12
            return max(1, months - 6), months + 6

        return None, None

    def _detect_temperament(self, query: str) -> Optional[str]:
        """Detect temperament keywords."""
        found = []
        for temp in TEMPERAMENT_KEYWORDS:
            if temp in query:
                found.append(temp)
        return ", ".join(found) if found else None

    def _generate_interpretation(self, filters: ParsedSearchFilters) -> str:
        """Generate human-readable interpretation of parsed filters."""
        parts = []
        if filters.color:
            parts.append(filters.color)
        if filters.breed:
            parts.append(filters.breed)
        if filters.gender:
            parts.append(filters.gender.capitalize())
        if filters.age_min_months:
            parts.append(f"aged {filters.age_min_months}-{filters.age_max_months} months")
        if filters.temperament:
            parts.append(f"({filters.temperament})")
        if filters.vaccinated:
            parts.append("vaccinated")
        if filters.neutered:
            parts.append("neutered")
        if filters.city:
            parts.append(f"near {filters.city}")

        if parts:
            return "Looking for: " + " ".join(parts)
        return f"Searching for: {filters.original_query}"


# Singleton
nlp_search_service = NLPSearchService()
