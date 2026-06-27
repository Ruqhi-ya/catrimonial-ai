# 🎬 Demo Script - Catrimonial AI

## Hackathon Presentation (5 minutes)

---

### Slide 1: Problem Statement (30s)

> "Did you know there are over 600 million pet cats worldwide, and responsible breeding is a growing concern? Cat owners struggle with finding compatible companions, managing health records, and connecting with nearby cat owners."

---

### Slide 2: Solution (30s)

> "Introducing **Catrimonial AI** — The Responsible Cat Owner Network. An AI-powered ecosystem that goes beyond matchmaking to provide comprehensive cat care."

---

### Slide 3: Live Demo (3 minutes)

#### Demo Flow:

**1. Landing Page (15s)**
- Show beautiful cat-themed UI with animations
- Highlight the cat-themed micro-interactions
- Show responsive design

**2. Registration (20s)**
- Quick sign-up flow
- Real-time password strength indicator
- Form validation in action

**3. Add Cat Profile (30s)**
- Create "Luna" — Persian, Female, 2 years
- Upload photo → AI analyzes breed
- Show verification badge system

**4. AI Compatibility Match (45s) ⭐**
- Show compatibility with "Leo" (another Persian)
- **Score: 92.5%**
- Walk through factors: Breed (95%), Age (85%), Health (100%), Distance (95%), Temperament (60%)
- Show recommendation text

**5. Natural Language Search (20s) ⭐**
- Type: "White Persian Female near Bangalore"
- AI parses: breed=Persian, gender=Female, color=White, city=Bangalore
- Show confidence score

**6. Health Assistant (30s) ⭐**
- Ask: "My cat is sleeping all day and not eating"
- Show AI response with causes, suggestions, urgency level
- Highlight the safety disclaimer

**7. Breeding Assessment (20s) ⭐**
- Run assessment on a young kitten (8 months)
- Show: ❌ Not eligible — "Cat is too young"
- Show detailed criteria breakdown

---

### Slide 4: Technical Architecture (30s)

> "Built with React + Spring Boot + Python FastAPI. 6 AI models, 20+ database tables, real-time messaging, Docker deployment."

Show architecture diagram.

---

### Slide 5: Impact & Future (30s)

> "Catrimonial promotes responsible pet ownership through AI. Future: mobile app, blockchain verification, IoT health monitoring, veterinary telehealth."

---

## Key Demo Talking Points

1. **AI is meaningful** — Not just a buzzword. Multi-factor scoring, NLP parsing, RAG health assistant, computer vision (breed detection)

2. **Responsible focus** — Breeding assessment prevents unhealthy breeding. Health tracking catches issues early.

3. **Production quality** — JWT auth, WebSocket messaging, Docker deployment, CI/CD, monitoring

4. **Beautiful UX** — Cat-themed animations, glassmorphism, 60fps Framer Motion, mobile-first design

5. **Complete ecosystem** — Not just matching: health, community, lost & found, adoption

---

## Quick API Demo Commands

```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Demo User","email":"demo@catrimonial.ai","password":"Demo@123!"}'

# 2. NLP Search (AI Service)
curl -X POST http://localhost:8000/api/ai/search/parse \
  -H "Content-Type: application/json" \
  -d '{"query":"White Persian Female near Bangalore"}'

# 3. Health Question (AI Service)
curl -X POST http://localhost:8000/api/ai/health/ask \
  -H "Content-Type: application/json" \
  -d '{"question":"My cat is sleeping all day and not eating"}'

# 4. Breeding Assessment (AI Service)
curl -X POST http://localhost:8000/api/ai/breeding/assess \
  -H "Content-Type: application/json" \
  -d '{"cat":{"id":"1","name":"Kitty","breed":"Persian","gender":"FEMALE","age_months":8,"weight_kg":2.0,"vaccinated":false},"vaccination_count":1}'
```

---

## Backup Demo (if live demo fails)

Have screenshots/recordings ready:
1. Landing page with animations
2. Dashboard with cat cards
3. AI match result screen
4. Health assistant conversation
5. NLP search parsing result
