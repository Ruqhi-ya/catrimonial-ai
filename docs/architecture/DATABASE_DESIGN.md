# 🗄️ Database Design - Catrimonial AI

## Entity Relationship Diagram

```
┌──────────────┐       ┌──────────────┐       ┌──────────────────┐
│    users     │       │    roles     │       │   user_roles     │
├──────────────┤       ├──────────────┤       ├──────────────────┤
│ id (PK)      │──┐    │ id (PK)      │──┐    │ user_id (FK)     │
│ email        │  │    │ name         │  │    │ role_id (FK)     │
│ password     │  │    └──────────────┘  │    └──────────────────┘
│ name         │  │                      │
│ phone        │  └──────────────────────┘
│ profile_image│
│ country      │    ┌──────────────────┐
│ state        │    │      cats        │
│ city         │    ├──────────────────┤
│ bio          │◄───│ owner_id (FK)    │
│ verified     │    │ id (PK)          │
│ enabled      │    │ name             │
│ created_at   │    │ breed            │
│ updated_at   │    │ gender           │
└──────────────┘    │ age_months       │
       │            │ weight_kg        │
       │            │ color            │
       │            │ vaccinated       │
       │            │ neutered         │
       │            │ temperament      │
       │            │ health_issues    │
       │            │ description      │
       │            │ latitude         │
       │            │ longitude        │
       │            │ city             │
       │            │ verification     │
       │            │ created_at       │
       │            └────────┬─────────┘
       │                     │
       │            ┌────────▼─────────┐
       │            │   cat_images     │
       │            ├──────────────────┤
       │            │ id (PK)          │
       │            │ cat_id (FK)      │
       │            │ url              │
       │            │ is_primary       │
       │            │ cloudinary_id    │
       │            │ created_at       │
       │            └──────────────────┘
       │
       │    ┌──────────────────┐     ┌──────────────────┐
       │    │  match_requests  │     │    messages      │
       │    ├──────────────────┤     ├──────────────────┤
       ├───►│ requester_id(FK) │     │ id (PK)          │
       │    │ target_id (FK)   │     │ sender_id (FK)   │
       │    │ requester_cat(FK)│     │ receiver_id (FK) │
       │    │ target_cat (FK)  │     │ content          │
       │    │ status           │     │ message_type     │
       │    │ compat_score     │     │ read             │
       │    │ created_at       │     │ created_at       │
       │    └──────────────────┘     └──────────────────┘
       │
       │    ┌──────────────────┐     ┌──────────────────┐
       │    │  vaccinations    │     │ health_records   │
       │    ├──────────────────┤     ├──────────────────┤
       │    │ id (PK)          │     │ id (PK)          │
       │    │ cat_id (FK)      │     │ cat_id (FK)      │
       │    │ vaccine_name     │     │ record_type      │
       │    │ date_given       │     │ title            │
       │    │ next_due_date    │     │ description      │
       │    │ vet_name         │     │ date             │
       │    │ batch_number     │     │ vet_name         │
       │    │ created_at       │     │ attachments      │
       │    └──────────────────┘     │ created_at       │
       │                             └──────────────────┘
       │
       │    ┌──────────────────┐     ┌──────────────────┐
       │    │  appointments    │     │ recommendations  │
       │    ├──────────────────┤     ├──────────────────┤
       │    │ id (PK)          │     │ id (PK)          │
       │    │ user_id (FK)     │     │ user_id (FK)     │
       │    │ cat_id (FK)      │     │ cat_id (FK)      │
       │    │ title            │     │ recommended_cat  │
       │    │ description      │     │ score            │
       │    │ appointment_date │     │ reason           │
       │    │ vet_name         │     │ type             │
       │    │ location         │     │ viewed           │
       │    │ status           │     │ created_at       │
       │    │ created_at       │     └──────────────────┘
       │    └──────────────────┘
       │
       │    ┌──────────────────┐     ┌──────────────────┐
       │    │  notifications   │     │ community_posts  │
       │    ├──────────────────┤     ├──────────────────┤
       │    │ id (PK)          │     │ id (PK)          │
       │    │ user_id (FK)     │     │ author_id (FK)   │
       │    │ title            │     │ title            │
       │    │ message          │     │ content          │
       │    │ type             │     │ post_type        │
       │    │ read             │     │ images           │
       │    │ action_url       │     │ likes_count      │
       │    │ created_at       │     │ comments_count   │
       │    └──────────────────┘     │ created_at       │
       │                             └────────┬─────────┘
       │                                      │
       │    ┌──────────────────┐     ┌────────▼─────────┐
       │    │   lost_found     │     │    comments      │
       │    ├──────────────────┤     ├──────────────────┤
       │    │ id (PK)          │     │ id (PK)          │
       │    │ user_id (FK)     │     │ post_id (FK)     │
       │    │ cat_name         │     │ author_id (FK)   │
       │    │ report_type      │     │ content          │
       │    │ description      │     │ created_at       │
       │    │ last_seen_loc    │     └──────────────────┘
       │    │ latitude         │
       │    │ longitude        │     ┌──────────────────┐
       │    │ images           │     │ adoption_posts   │
       │    │ contact_info     │     ├──────────────────┤
       │    │ status           │     │ id (PK)          │
       │    │ created_at       │     │ user_id (FK)     │
       │    └──────────────────┘     │ cat_id (FK)      │
       │                             │ title            │
       │    ┌──────────────────┐     │ description      │
       │    │    reports       │     │ requirements     │
       │    ├──────────────────┤     │ status           │
       │    │ id (PK)          │     │ created_at       │
       │    │ reporter_id (FK) │     └──────────────────┘
       │    │ reported_type    │
       │    │ reported_id      │     ┌──────────────────┐
       │    │ reason           │     │   audit_logs     │
       │    │ description      │     ├──────────────────┤
       │    │ status           │     │ id (PK)          │
       │    │ created_at       │     │ user_id (FK)     │
       │    └──────────────────┘     │ action           │
       │                             │ entity_type      │
       │                             │ entity_id        │
       │                             │ details          │
       │                             │ ip_address       │
       │                             │ created_at       │
       │                             └──────────────────┘
       │
       │    ┌──────────────────┐
       │    │ post_likes       │
       │    ├──────────────────┤
       └───►│ user_id (FK)     │
            │ post_id (FK)     │
            │ created_at       │
            └──────────────────┘
```

## Table Specifications

### Core Tables

| Table | Description | Estimated Rows |
|-------|-------------|---------------|
| users | User accounts | 100K+ |
| roles | System roles | 3 |
| cats | Cat profiles | 200K+ |
| cat_images | Cat photos | 500K+ |
| match_requests | Compatibility matches | 1M+ |
| messages | Chat messages | 10M+ |
| vaccinations | Vaccination records | 500K+ |
| health_records | Medical history | 300K+ |
| appointments | Vet appointments | 200K+ |
| recommendations | AI recommendations | 2M+ |
| notifications | User notifications | 5M+ |
| community_posts | Community content | 100K+ |
| comments | Post comments | 500K+ |
| lost_found | Missing/found reports | 50K+ |
| adoption_posts | Adoption listings | 30K+ |
| reports | User reports | 10K+ |
| audit_logs | System audit trail | 50M+ |

### Indexes Strategy

| Table | Index | Type | Purpose |
|-------|-------|------|---------|
| users | email | UNIQUE | Login lookup |
| users | city, state | BTREE | Location queries |
| cats | owner_id | BTREE | Owner's cats |
| cats | breed, gender | BTREE | Search filters |
| cats | (latitude, longitude) | GIST | Geo queries |
| messages | sender_id, receiver_id | BTREE | Conversation lookup |
| messages | created_at | BTREE DESC | Recent messages |
| notifications | user_id, read | BTREE | Unread count |
| community_posts | author_id | BTREE | User's posts |
| community_posts | created_at | BTREE DESC | Feed ordering |
| audit_logs | user_id, created_at | BTREE | User activity |
