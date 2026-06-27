"""
AI Health Assistant Service
RAG-based veterinary Q&A with guardrails and safety disclaimers.
Uses a knowledge base of cat health information.
"""

import logging
from typing import List, Optional
from app.models.schemas import CatProfile, HealthResponse

logger = logging.getLogger(__name__)

# Cat health knowledge base (in production, this would be a vector store)
HEALTH_KNOWLEDGE = {
    "not eating": {
        "causes": [
            "Dental problems or tooth pain",
            "Gastrointestinal issues (nausea, inflammation)",
            "Stress or anxiety from environmental changes",
            "Upper respiratory infection reducing sense of smell",
            "Kidney disease (especially in older cats)",
            "Food aversion or boredom with current diet",
        ],
        "suggestions": [
            "Try warming the food slightly to enhance aroma",
            "Offer a different protein source or wet food",
            "Ensure fresh water is available at all times",
            "Check for any dental issues (drooling, bad breath)",
            "Remove stressors from the environment",
            "If persists more than 24-48 hours, see a vet immediately",
        ],
        "urgency": "MEDIUM",
        "visit_vet": True,
    },
    "sleeping": {
        "causes": [
            "Normal behavior - cats sleep 12-16 hours daily",
            "Age-related increase in sleep (senior cats)",
            "Boredom or lack of stimulation",
            "Illness or pain causing lethargy",
            "Anemia or thyroid issues",
            "Depression from loss of companion",
        ],
        "suggestions": [
            "Monitor for other symptoms (not eating, hiding)",
            "Provide interactive toys and play sessions",
            "Ensure environment is enriching",
            "If combined with appetite loss, see a vet",
            "Track sleep patterns for changes",
        ],
        "urgency": "LOW",
        "visit_vet": False,
    },
    "vomiting": {
        "causes": [
            "Hairballs (especially in long-haired cats)",
            "Eating too quickly",
            "Food allergies or intolerance",
            "Ingestion of toxic substances or foreign objects",
            "Gastrointestinal inflammation",
            "Parasites or infections",
        ],
        "suggestions": [
            "Withhold food for 12 hours, then offer small bland meals",
            "Use slow-feeder bowls to prevent fast eating",
            "Regular brushing to reduce hairballs",
            "Remove any toxic plants or chemicals from access",
            "If vomiting persists over 24 hours or contains blood, seek emergency vet care",
        ],
        "urgency": "MEDIUM",
        "visit_vet": True,
    },
    "scratching": {
        "causes": [
            "Fleas or other parasites",
            "Allergies (food, environmental, or contact)",
            "Dry skin or fungal infections",
            "Ear mites (if scratching ears)",
            "Stress-related over-grooming",
        ],
        "suggestions": [
            "Check for flea dirt (small black specks in fur)",
            "Keep environment clean and use flea prevention",
            "Try an elimination diet to rule out food allergies",
            "Use a humidifier if air is dry",
            "Consult vet for persistent scratching or hair loss",
        ],
        "urgency": "LOW",
        "visit_vet": False,
    },
    "breathing": {
        "causes": [
            "Asthma or bronchitis",
            "Upper respiratory infection",
            "Heart disease or fluid in lungs",
            "Allergic reaction",
            "Obstruction in airway",
            "Stress or overheating",
        ],
        "suggestions": [
            "Keep the cat calm and in a cool area",
            "Do NOT give any medications without vet guidance",
            "If open-mouth breathing or blue gums, seek EMERGENCY care",
            "Note the breathing rate (normal is 15-30 breaths/min at rest)",
            "Remove any potential irritants (smoke, strong scents)",
        ],
        "urgency": "HIGH",
        "visit_vet": True,
    },
    "limping": {
        "causes": [
            "Sprain or strain from jumping",
            "Wound or foreign object in paw",
            "Arthritis (especially in older cats)",
            "Fracture or dislocation",
            "Ingrown or overgrown nails",
        ],
        "suggestions": [
            "Gently examine the affected leg and paw",
            "Look for swelling, wounds, or objects stuck in paw",
            "Restrict activity and jumping",
            "Apply cold compress if swelling (not directly on skin)",
            "If no improvement in 24-48 hours, see a vet",
        ],
        "urgency": "MEDIUM",
        "visit_vet": True,
    },
    "hiding": {
        "causes": [
            "Fear or stress (new environment, visitors, loud noises)",
            "Illness - cats often hide when feeling unwell",
            "Pain from injury or internal issue",
            "Normal personality trait for some cats",
            "New pet or family member causing anxiety",
        ],
        "suggestions": [
            "Provide safe hiding spots (cat caves, boxes)",
            "Don't force the cat out - let them come on their own",
            "Maintain routine feeding and litter schedules",
            "Use calming pheromone diffusers (Feliway)",
            "If hiding is sudden and prolonged (>48 hours), consult a vet",
        ],
        "urgency": "LOW",
        "visit_vet": False,
    },
}

# Emergency keywords that trigger HIGH urgency
EMERGENCY_KEYWORDS = [
    "blood", "bleeding", "seizure", "collapse", "unconscious",
    "poisoned", "poison", "toxin", "not breathing", "blue gums",
    "hit by car", "fall", "broken", "fracture", "convulsing",
    "choking", "swallowed", "ingested",
]

DISCLAIMER = (
    "⚠️ This is AI-generated health information for educational purposes only. "
    "It is NOT a substitute for professional veterinary advice, diagnosis, or treatment. "
    "Always consult a qualified veterinarian for your cat's specific health concerns."
)


class HealthAssistantService:
    """RAG-based cat health Q&A assistant."""

    def answer_question(self, question: str, cat_info: Optional[CatProfile] = None) -> HealthResponse:
        """Answer a health-related question about cats."""
        question_lower = question.lower()

        # Check for emergency keywords
        is_emergency = any(kw in question_lower for kw in EMERGENCY_KEYWORDS)
        if is_emergency:
            return self._emergency_response(question)

        # Find relevant knowledge
        best_match_key = None
        best_match_score = 0

        for key in HEALTH_KNOWLEDGE:
            if key in question_lower:
                score = len(key)
                if score > best_match_score:
                    best_match_score = score
                    best_match_key = key

        if best_match_key:
            knowledge = HEALTH_KNOWLEDGE[best_match_key]
            return self._build_response(question, knowledge, cat_info)

        # Generic response when no specific match
        return self._generic_response(question, cat_info)

    def _emergency_response(self, question: str) -> HealthResponse:
        """Response for emergency situations."""
        return HealthResponse(
            answer=(
                "🚨 This sounds like it could be an emergency situation. "
                "Please contact your veterinarian or emergency animal hospital IMMEDIATELY. "
                "Time is critical in these situations."
            ),
            possible_causes=["This requires immediate professional assessment"],
            suggestions=[
                "Call your vet or emergency animal hospital NOW",
                "Keep your cat calm and warm",
                "Do not give any medication without vet advice",
                "If bleeding, apply gentle pressure with clean cloth",
                "Transport carefully to the nearest vet",
            ],
            urgency_level="EMERGENCY",
            visit_vet=True,
            disclaimer=DISCLAIMER
        )

    def _build_response(self, question: str, knowledge: dict, cat_info: Optional[CatProfile]) -> HealthResponse:
        """Build response from knowledge base match."""
        causes = knowledge["causes"]
        suggestions = knowledge["suggestions"]
        urgency = knowledge["urgency"]
        visit_vet = knowledge["visit_vet"]

        # Personalize based on cat info
        answer_parts = []
        if cat_info:
            answer_parts.append(f"Based on what you've described for {cat_info.name}")
            if cat_info.age_months and cat_info.age_months > 120:
                answer_parts.append(
                    "Since your cat is a senior, some symptoms may require extra attention."
                )
                urgency = "MEDIUM" if urgency == "LOW" else urgency

        answer_parts.append(
            "Here are possible causes and what you can do:"
        )

        return HealthResponse(
            answer=" ".join(answer_parts),
            possible_causes=causes,
            suggestions=suggestions,
            urgency_level=urgency,
            visit_vet=visit_vet,
            disclaimer=DISCLAIMER
        )

    def _generic_response(self, question: str, cat_info: Optional[CatProfile]) -> HealthResponse:
        """Generic response when no specific match found."""
        return HealthResponse(
            answer=(
                "I understand you have a health concern about your cat. "
                "While I can provide general guidance, this question may require "
                "a professional veterinary assessment for a proper answer."
            ),
            possible_causes=[
                "Multiple factors could be involved",
                "A vet examination can properly diagnose the issue",
            ],
            suggestions=[
                "Monitor your cat's behavior for 24-48 hours",
                "Note any changes in eating, drinking, or litter habits",
                "Take photos or videos of symptoms to show your vet",
                "Schedule a veterinary check-up",
                "Keep a symptom diary with dates and observations",
            ],
            urgency_level="LOW",
            visit_vet=True,
            disclaimer=DISCLAIMER
        )


# Singleton
health_assistant = HealthAssistantService()
