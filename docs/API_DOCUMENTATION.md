# 📡 API Documentation - Catrimonial AI

## Base URLs

| Service | URL | Description |
|---------|-----|-------------|
| Backend API | `http://localhost:8080/api` | Spring Boot REST API |
| AI Service | `http://localhost:8000` | Python FastAPI AI endpoints |
| Swagger UI | `http://localhost:8080/api/swagger-ui.html` | Interactive API docs |
| AI Docs | `http://localhost:8000/docs` | FastAPI auto-docs |

## Authentication

All authenticated endpoints require a Bearer token:
```
Authorization: Bearer <access_token>
```

---

## 🔐 Auth Module

### POST `/auth/register`
Create a new user account.

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Str0ng!Pass",
  "phone": "+91-9876543210",
  "city": "Bangalore",
  "state": "Karnataka",
  "country": "India"
}
```

**Response (201):**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "a1b2c3d4...",
  "tokenType": "Bearer",
  "expiresIn": 900000,
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "verified": false,
    "roles": ["ROLE_USER"]
  }
}
```

### POST `/auth/login`
Authenticate with email and password.

**Request:**
```json
{
  "email": "john@example.com",
  "password": "Str0ng!Pass"
}
```

### POST `/auth/refresh-token`
Get new tokens using refresh token.

**Request:**
```json
{
  "refreshToken": "a1b2c3d4..."
}
```

### POST `/auth/forgot-password`
Request password reset email.

### POST `/auth/reset-password`
Reset password with token.

### GET `/auth/verify-email?token=xxx`
Verify email address.

---

## 🐱 Cat Management

### POST `/cats`
Create a new cat profile. **(Auth required)**

**Request:**
```json
{
  "name": "Luna",
  "breed": "Persian",
  "gender": "FEMALE",
  "ageMonths": 24,
  "weightKg": 4.2,
  "color": "White",
  "vaccinated": true,
  "neutered": true,
  "temperament": "calm, friendly",
  "description": "A beautiful Persian with blue eyes",
  "city": "Bangalore",
  "state": "Karnataka",
  "country": "India"
}
```

### GET `/cats/my-cats?page=0&size=20`
Get current user's cats. **(Auth required)**

### GET `/cats/{catId}`
Get a cat profile by ID.

### PUT `/cats/{catId}`
Update a cat profile. **(Auth required, owner only)**

### DELETE `/cats/{catId}`
Soft-delete a cat profile. **(Auth required, owner only)**

### GET `/cats/public/search?breed=Persian&gender=FEMALE&city=Bangalore`
Search cats with filters.

### GET `/cats/public/nearby?city=Bangalore`
Get nearby cats.

### GET `/cats/public/breeds`
Get list of all available breeds.

---

## 💊 Health Management

### POST `/health/vaccinations`
Add vaccination record. **(Auth required)**

### GET `/health/vaccinations/cat/{catId}`
Get vaccinations for a cat.

### GET `/health/vaccinations/upcoming`
Get upcoming vaccination reminders.

### POST `/health/records`
Add health record. **(Auth required)**

### GET `/health/records/cat/{catId}?type=CHECKUP`
Get health records for a cat.

### POST `/health/appointments`
Create vet appointment. **(Auth required)**

### GET `/health/appointments/upcoming`
Get upcoming appointments.

---

## 💬 Messaging

### POST `/messages`
Send a message. **(Auth required)**

### GET `/messages/conversation/{otherUserId}?page=0`
Get conversation messages.

### GET `/messages/conversations`
Get all user conversations.

### POST `/messages/read/{senderId}`
Mark messages as read.

### GET `/messages/unread-count`
Get unread message count.

### WebSocket: `ws://localhost:8080/ws`
Real-time messaging via STOMP over WebSocket.

---

## 👥 Community

### POST `/community/posts`
Create a post. **(Auth required)**

### GET `/community/posts?type=QUESTION&page=0`
Get community feed.

### GET `/community/posts/popular`
Get popular posts.

### POST `/community/posts/{postId}/like`
Toggle like on a post.

### POST `/community/posts/{postId}/comments`
Add a comment.

---

## 🤖 AI Service Endpoints

### POST `/api/ai/compatibility`
Calculate compatibility between two cats.

**Request:**
```json
{
  "cat1": {
    "id": "uuid1",
    "name": "Luna",
    "breed": "Persian",
    "gender": "FEMALE",
    "age_months": 24,
    "vaccinated": true,
    "city": "Bangalore"
  },
  "cat2": {
    "id": "uuid2",
    "name": "Leo",
    "breed": "Persian",
    "gender": "MALE",
    "age_months": 28,
    "vaccinated": true,
    "city": "Bangalore"
  }
}
```

**Response:**
```json
{
  "overall_score": 92.5,
  "factors": [
    {"factor": "Breed Compatibility", "score": 95, "weight": 0.4, "positive": true},
    {"factor": "Age Compatibility", "score": 85, "weight": 0.2, "positive": true},
    {"factor": "Health Status", "score": 100, "weight": 0.2, "positive": true},
    {"factor": "Distance", "score": 95, "weight": 0.1, "positive": true},
    {"factor": "Temperament", "score": 60, "weight": 0.1, "positive": true}
  ],
  "recommendation": "Excellent Match! These cats are highly compatible.",
  "breeding_compatible": true,
  "reasoning": "Luna ❤️ Leo\n\nStrengths:\n✔ Same breed (Persian)..."
}
```

### POST `/api/ai/search/parse`
Parse natural language search query.

**Request:**
```json
{
  "query": "White Persian Female near Bangalore"
}
```

**Response:**
```json
{
  "filters": {
    "breed": "Persian",
    "gender": "FEMALE",
    "color": "White",
    "city": "Bangalore"
  },
  "confidence": 0.85,
  "interpreted_as": "Looking for: White Persian Female near Bangalore"
}
```

### POST `/api/ai/health/ask`
Ask the AI health assistant.

### POST `/api/ai/breeding/assess`
Assess breeding eligibility.

### POST `/api/ai/recommendations`
Get personalized recommendations.

### POST `/api/ai/photo/analyze`
Analyze a cat photo (multipart/form-data).

---

## 📊 Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request (validation error) |
| 401 | Unauthorized (invalid/missing token) |
| 403 | Forbidden (insufficient permissions) |
| 404 | Resource not found |
| 409 | Conflict (duplicate resource) |
| 413 | Payload too large |
| 429 | Rate limited |
| 500 | Internal server error |
