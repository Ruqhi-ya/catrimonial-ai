"""
Responsible Breeding Assessment Service
Evaluates whether a cat is eligible for breeding based on health, age, and vaccination criteria.
"""

import logging
from typing import List, Dict
from app.models.schemas import CatProfile, BreedingAssessmentResponse

logger = logging.getLogger(__name__)

# Breeding criteria thresholds
MIN_BREEDING_AGE_MONTHS = 12  # Minimum 1 year old
MAX_BREEDING_AGE_MONTHS = 84  # Maximum 7 years old
MIN_WEIGHT_KG = {
    "FEMALE": 2.5,
    "MALE": 3.0,
}
REQUIRED_VACCINATIONS = 3  # Minimum number of vaccinations


class BreedingAdvisorService:
    """Responsible breeding assessment engine."""

    def assess(self, cat: CatProfile, vaccination_count: int = 0,
               last_health_check_days_ago: int = None) -> BreedingAssessmentResponse:
        """Assess a cat's breeding eligibility."""
        reasons: List[Dict] = []
        score = 100.0

        # 1. Age assessment
        age_eligible = self._assess_age(cat, reasons)
        if not age_eligible:
            score -= 30

        # 2. Weight assessment
        weight_eligible = self._assess_weight(cat, reasons)
        if not weight_eligible:
            score -= 20

        # 3. Vaccination assessment
        vaccination_complete = self._assess_vaccinations(cat, vaccination_count, reasons)
        if not vaccination_complete:
            score -= 25

        # 4. Health assessment
        no_health_issues = self._assess_health(cat, reasons)
        if not no_health_issues:
            score -= 25

        # 5. Recent health check
        if last_health_check_days_ago is not None and last_health_check_days_ago > 180:
            reasons.append({
                "criterion": "Recent Health Check",
                "passed": False,
                "message": f"Last health check was {last_health_check_days_ago} days ago. A recent check (within 6 months) is recommended.",
                "severity": "WARNING"
            })
            score -= 10

        # Calculate overall eligibility
        score = max(score, 0.0)
        is_eligible = (age_eligible and weight_eligible and
                       vaccination_complete and no_health_issues and score >= 70)

        # Generate recommendation
        recommendation = self._generate_recommendation(
            is_eligible, cat, reasons, score
        )

        # Calculate wait time if not eligible
        minimum_wait = None
        if not is_eligible and not age_eligible and cat.age_months < MIN_BREEDING_AGE_MONTHS:
            minimum_wait = MIN_BREEDING_AGE_MONTHS - cat.age_months

        return BreedingAssessmentResponse(
            is_eligible=is_eligible,
            overall_score=round(score, 1),
            reasons=reasons,
            recommendation=recommendation,
            age_eligible=age_eligible,
            weight_eligible=weight_eligible,
            vaccination_complete=vaccination_complete,
            no_health_issues=no_health_issues,
            minimum_wait_months=minimum_wait
        )

    def _assess_age(self, cat: CatProfile, reasons: List[Dict]) -> bool:
        """Assess age eligibility."""
        if cat.age_months < MIN_BREEDING_AGE_MONTHS:
            reasons.append({
                "criterion": "Age",
                "passed": False,
                "message": f"Cat is {cat.age_months} months old. Minimum breeding age is {MIN_BREEDING_AGE_MONTHS} months (1 year).",
                "severity": "CRITICAL"
            })
            return False
        elif cat.age_months > MAX_BREEDING_AGE_MONTHS:
            reasons.append({
                "criterion": "Age",
                "passed": False,
                "message": f"Cat is {cat.age_months // 12} years old. Maximum recommended breeding age is {MAX_BREEDING_AGE_MONTHS // 12} years.",
                "severity": "WARNING"
            })
            return False
        else:
            reasons.append({
                "criterion": "Age",
                "passed": True,
                "message": f"Cat is {cat.age_months} months old - within optimal breeding age range.",
                "severity": "OK"
            })
            return True

    def _assess_weight(self, cat: CatProfile, reasons: List[Dict]) -> bool:
        """Assess weight eligibility."""
        if cat.weight_kg is None:
            reasons.append({
                "criterion": "Weight",
                "passed": False,
                "message": "Weight information not available. Please weigh your cat.",
                "severity": "WARNING"
            })
            return False

        min_weight = MIN_WEIGHT_KG.get(cat.gender.value, 2.5)

        if cat.weight_kg < min_weight:
            reasons.append({
                "criterion": "Weight",
                "passed": False,
                "message": f"Cat weighs {cat.weight_kg}kg. Minimum weight for breeding is {min_weight}kg for {cat.gender.value.lower()} cats.",
                "severity": "CRITICAL"
            })
            return False
        else:
            reasons.append({
                "criterion": "Weight",
                "passed": True,
                "message": f"Cat weighs {cat.weight_kg}kg - adequate weight for breeding.",
                "severity": "OK"
            })
            return True

    def _assess_vaccinations(self, cat: CatProfile, vaccination_count: int, reasons: List[Dict]) -> bool:
        """Assess vaccination completeness."""
        if not cat.vaccinated:
            reasons.append({
                "criterion": "Vaccination",
                "passed": False,
                "message": "Cat is not vaccinated. Core vaccinations (FVRCP, Rabies) are required before breeding.",
                "severity": "CRITICAL"
            })
            return False

        if vaccination_count < REQUIRED_VACCINATIONS:
            reasons.append({
                "criterion": "Vaccination",
                "passed": False,
                "message": f"Only {vaccination_count} vaccinations recorded. Minimum {REQUIRED_VACCINATIONS} required (FVRCP, Rabies, FeLV).",
                "severity": "WARNING"
            })
            return False

        reasons.append({
            "criterion": "Vaccination",
            "passed": True,
            "message": "Vaccination records are complete.",
            "severity": "OK"
        })
        return True

    def _assess_health(self, cat: CatProfile, reasons: List[Dict]) -> bool:
        """Assess general health status."""
        if cat.health_issues and cat.health_issues.strip():
            reasons.append({
                "criterion": "Health",
                "passed": False,
                "message": f"Health issues detected: {cat.health_issues}. Breeding not recommended until resolved.",
                "severity": "CRITICAL"
            })
            return False

        reasons.append({
            "criterion": "Health",
            "passed": True,
            "message": "No known health issues.",
            "severity": "OK"
        })
        return True

    def _generate_recommendation(self, is_eligible: bool, cat: CatProfile,
                                  reasons: List[Dict], score: float) -> str:
        """Generate a human-readable recommendation."""
        if is_eligible:
            return (
                f"✅ {cat.name} appears eligible for responsible breeding. "
                f"Overall health and readiness score: {score:.0f}/100. "
                "We recommend a pre-breeding veterinary examination to confirm fitness."
            )

        critical_issues = [r for r in reasons if r["severity"] == "CRITICAL" and not r["passed"]]
        warnings = [r for r in reasons if r["severity"] == "WARNING" and not r["passed"]]

        parts = [f"❌ Breeding is NOT recommended for {cat.name} at this time."]

        if critical_issues:
            parts.append("Critical issues:")
            for issue in critical_issues:
                parts.append(f"  • {issue['message']}")

        if warnings:
            parts.append("Warnings:")
            for warn in warnings:
                parts.append(f"  • {warn['message']}")

        parts.append(
            "\nPlease address these concerns before considering breeding. "
            "Consult with a veterinarian for professional guidance."
        )

        return "\n".join(parts)


# Singleton
breeding_advisor = BreedingAdvisorService()
