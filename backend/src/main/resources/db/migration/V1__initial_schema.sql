-- ============================================
-- CATRIMONIAL AI - Database Schema
-- Version: 1.0.0
-- Database: PostgreSQL 15
-- ============================================

-- Enable extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ============================================
-- ROLES TABLE
-- ============================================
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

INSERT INTO roles (name, description) VALUES
    ('ROLE_USER', 'Standard user role'),
    ('ROLE_ADMIN', 'Administrator role'),
    ('ROLE_VETERINARIAN', 'Veterinarian role');

-- ============================================
-- USERS TABLE
-- ============================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    profile_image VARCHAR(500),
    country VARCHAR(100),
    state VARCHAR(100),
    city VARCHAR(100),
    bio TEXT,
    verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT FALSE,
    email_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_password_token VARCHAR(255),
    reset_password_expires TIMESTAMP WITH TIME ZONE,
    last_login TIMESTAMP WITH TIME ZONE,
    profile_completion_score INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_city ON users(city);
CREATE INDEX idx_users_verified ON users(verified);

-- ============================================
-- USER_ROLES TABLE
-- ============================================
CREATE TABLE user_roles (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ============================================
-- REFRESH TOKENS TABLE
-- ============================================
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- ============================================
-- CATS TABLE
-- ============================================
CREATE TABLE cats (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    breed VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    age_months INTEGER NOT NULL CHECK (age_months > 0),
    weight_kg DECIMAL(5,2) CHECK (weight_kg > 0),
    color VARCHAR(100),
    vaccinated BOOLEAN DEFAULT FALSE,
    neutered BOOLEAN DEFAULT FALSE,
    temperament VARCHAR(255),
    health_issues TEXT,
    description TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    verification_status VARCHAR(20) DEFAULT 'PENDING' CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_cats_owner ON cats(owner_id);
CREATE INDEX idx_cats_breed ON cats(breed);
CREATE INDEX idx_cats_gender ON cats(gender);
CREATE INDEX idx_cats_breed_gender ON cats(breed, gender);
CREATE INDEX idx_cats_city ON cats(city);
CREATE INDEX idx_cats_location ON cats USING GIST (
    ST_MakePoint(longitude, latitude)
);
CREATE INDEX idx_cats_active ON cats(active);
CREATE INDEX idx_cats_verification ON cats(verification_status);

-- ============================================
-- CAT IMAGES TABLE
-- ============================================
CREATE TABLE cat_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    url VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(500),
    cloudinary_public_id VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    ai_breed_detection VARCHAR(100),
    ai_health_score DECIMAL(3,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_cat_images_cat ON cat_images(cat_id);
CREATE INDEX idx_cat_images_primary ON cat_images(cat_id, is_primary);

-- ============================================
-- MATCH REQUESTS TABLE
-- ============================================
CREATE TABLE match_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    requester_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    requester_cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    target_cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    compatibility_score DECIMAL(5,2),
    compatibility_reasons JSONB,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'EXPIRED')),
    message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_match_requester ON match_requests(requester_id);
CREATE INDEX idx_match_target ON match_requests(target_id);
CREATE INDEX idx_match_status ON match_requests(status);
CREATE INDEX idx_match_score ON match_requests(compatibility_score DESC);

-- ============================================
-- MESSAGES TABLE
-- ============================================
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    receiver_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    message_type VARCHAR(20) DEFAULT 'TEXT' CHECK (message_type IN ('TEXT', 'IMAGE', 'SYSTEM')),
    image_url VARCHAR(500),
    read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_messages_conversation ON messages(sender_id, receiver_id, created_at DESC);
CREATE INDEX idx_messages_receiver_unread ON messages(receiver_id, read) WHERE read = FALSE;
CREATE INDEX idx_messages_created ON messages(created_at DESC);

-- ============================================
-- VACCINATIONS TABLE
-- ============================================
CREATE TABLE vaccinations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    vaccine_name VARCHAR(100) NOT NULL,
    date_given DATE NOT NULL,
    next_due_date DATE,
    vet_name VARCHAR(100),
    vet_clinic VARCHAR(200),
    batch_number VARCHAR(100),
    notes TEXT,
    document_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_vaccinations_cat ON vaccinations(cat_id);
CREATE INDEX idx_vaccinations_due ON vaccinations(next_due_date);

-- ============================================
-- HEALTH RECORDS TABLE
-- ============================================
CREATE TABLE health_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    record_type VARCHAR(50) NOT NULL CHECK (record_type IN ('CHECKUP', 'SURGERY', 'MEDICATION', 'ALLERGY', 'DEWORMING', 'HEAT_CYCLE', 'OTHER')),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    record_date DATE NOT NULL,
    vet_name VARCHAR(100),
    vet_clinic VARCHAR(200),
    attachments JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_health_records_cat ON health_records(cat_id);
CREATE INDEX idx_health_records_type ON health_records(cat_id, record_type);
CREATE INDEX idx_health_records_date ON health_records(record_date DESC);

-- ============================================
-- APPOINTMENTS TABLE
-- ============================================
CREATE TABLE appointments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    appointment_date TIMESTAMP WITH TIME ZONE NOT NULL,
    vet_name VARCHAR(100),
    vet_clinic VARCHAR(200),
    location VARCHAR(300),
    status VARCHAR(20) DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'MISSED')),
    reminder_sent BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_appointments_user ON appointments(user_id);
CREATE INDEX idx_appointments_cat ON appointments(cat_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);

-- ============================================
-- RECOMMENDATIONS TABLE
-- ============================================
CREATE TABLE recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    recommended_cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    score DECIMAL(5,2) NOT NULL,
    reason TEXT,
    recommendation_type VARCHAR(30) DEFAULT 'COMPATIBILITY' CHECK (recommendation_type IN ('COMPATIBILITY', 'SIMILAR', 'POPULAR', 'NEARBY')),
    viewed BOOLEAN DEFAULT FALSE,
    dismissed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_recommendations_user ON recommendations(user_id);
CREATE INDEX idx_recommendations_cat ON recommendations(cat_id);
CREATE INDEX idx_recommendations_score ON recommendations(score DESC);

-- ============================================
-- NOTIFICATIONS TABLE
-- ============================================
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(30) NOT NULL CHECK (notification_type IN ('MATCH', 'MESSAGE', 'HEALTH', 'VACCINATION', 'COMMUNITY', 'SYSTEM', 'LOST_FOUND', 'ADOPTION')),
    read BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(500),
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_unread ON notifications(user_id, read) WHERE read = FALSE;
CREATE INDEX idx_notifications_type ON notifications(notification_type);
CREATE INDEX idx_notifications_created ON notifications(created_at DESC);

-- ============================================
-- COMMUNITY POSTS TABLE
-- ============================================
CREATE TABLE community_posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(300) NOT NULL,
    content TEXT NOT NULL,
    post_type VARCHAR(30) DEFAULT 'GENERAL' CHECK (post_type IN ('GENERAL', 'QUESTION', 'SUCCESS_STORY', 'TIP', 'EVENT', 'DISCUSSION')),
    images JSONB,
    tags VARCHAR(500),
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    pinned BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_posts_author ON community_posts(author_id);
CREATE INDEX idx_posts_type ON community_posts(post_type);
CREATE INDEX idx_posts_created ON community_posts(created_at DESC);
CREATE INDEX idx_posts_popular ON community_posts(likes_count DESC);

-- ============================================
-- COMMENTS TABLE
-- ============================================
CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id UUID NOT NULL REFERENCES community_posts(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    parent_id UUID REFERENCES comments(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    likes_count INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_comments_post ON comments(post_id);
CREATE INDEX idx_comments_author ON comments(author_id);
CREATE INDEX idx_comments_parent ON comments(parent_id);

-- ============================================
-- POST LIKES TABLE
-- ============================================
CREATE TABLE post_likes (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID REFERENCES community_posts(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (user_id, post_id)
);

-- ============================================
-- LOST & FOUND TABLE
-- ============================================
CREATE TABLE lost_found (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cat_id UUID REFERENCES cats(id) ON DELETE SET NULL,
    cat_name VARCHAR(100) NOT NULL,
    report_type VARCHAR(10) NOT NULL CHECK (report_type IN ('LOST', 'FOUND')),
    description TEXT NOT NULL,
    breed VARCHAR(100),
    color VARCHAR(100),
    gender VARCHAR(10),
    last_seen_location VARCHAR(300),
    last_seen_date DATE,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    images JSONB,
    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),
    reward_offered BOOLEAN DEFAULT FALSE,
    reward_amount DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'RESOLVED', 'EXPIRED')),
    resolved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_lost_found_user ON lost_found(user_id);
CREATE INDEX idx_lost_found_type ON lost_found(report_type);
CREATE INDEX idx_lost_found_status ON lost_found(status);
CREATE INDEX idx_lost_found_location ON lost_found USING GIST (
    ST_MakePoint(longitude, latitude)
);
CREATE INDEX idx_lost_found_date ON lost_found(created_at DESC);

-- ============================================
-- ADOPTION POSTS TABLE
-- ============================================
CREATE TABLE adoption_posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cat_id UUID REFERENCES cats(id) ON DELETE SET NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT NOT NULL,
    cat_name VARCHAR(100) NOT NULL,
    breed VARCHAR(100),
    age_months INTEGER,
    gender VARCHAR(10),
    vaccinated BOOLEAN DEFAULT FALSE,
    neutered BOOLEAN DEFAULT FALSE,
    special_needs TEXT,
    requirements TEXT,
    adoption_fee DECIMAL(10,2) DEFAULT 0,
    images JSONB,
    location VARCHAR(300),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'PENDING', 'ADOPTED', 'CLOSED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_adoption_user ON adoption_posts(user_id);
CREATE INDEX idx_adoption_status ON adoption_posts(status);
CREATE INDEX idx_adoption_location ON adoption_posts USING GIST (
    ST_MakePoint(longitude, latitude)
);
CREATE INDEX idx_adoption_created ON adoption_posts(created_at DESC);

-- ============================================
-- REPORTS TABLE
-- ============================================
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reporter_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reported_entity_type VARCHAR(30) NOT NULL CHECK (reported_entity_type IN ('USER', 'CAT', 'POST', 'COMMENT', 'MESSAGE')),
    reported_entity_id UUID NOT NULL,
    reason VARCHAR(50) NOT NULL CHECK (reason IN ('SPAM', 'INAPPROPRIATE', 'FAKE_PROFILE', 'HARASSMENT', 'SCAM', 'ANIMAL_ABUSE', 'OTHER')),
    description TEXT,
    evidence_urls JSONB,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'REVIEWING', 'RESOLVED', 'DISMISSED')),
    admin_notes TEXT,
    resolved_by UUID REFERENCES users(id),
    resolved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_reports_reporter ON reports(reporter_id);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_entity ON reports(reported_entity_type, reported_entity_id);

-- ============================================
-- AUDIT LOGS TABLE
-- ============================================
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID,
    details JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_action ON audit_logs(action);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_created ON audit_logs(created_at DESC);

-- ============================================
-- CONVERSATIONS TABLE (for messaging optimization)
-- ============================================
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user1_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    user2_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    last_message_id UUID,
    last_message_at TIMESTAMP WITH TIME ZONE,
    user1_unread_count INTEGER DEFAULT 0,
    user2_unread_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user1_id, user2_id)
);

CREATE INDEX idx_conversations_user1 ON conversations(user1_id);
CREATE INDEX idx_conversations_user2 ON conversations(user2_id);
CREATE INDEX idx_conversations_last_msg ON conversations(last_message_at DESC);

-- ============================================
-- BREEDING ASSESSMENTS TABLE
-- ============================================
CREATE TABLE breeding_assessments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cat_id UUID NOT NULL REFERENCES cats(id) ON DELETE CASCADE,
    assessed_by UUID REFERENCES users(id),
    is_eligible BOOLEAN NOT NULL,
    reasons JSONB NOT NULL,
    health_score DECIMAL(3,2),
    age_eligible BOOLEAN,
    weight_eligible BOOLEAN,
    vaccination_complete BOOLEAN,
    no_health_issues BOOLEAN,
    recommendation TEXT,
    valid_until DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_breeding_cat ON breeding_assessments(cat_id);
CREATE INDEX idx_breeding_eligible ON breeding_assessments(is_eligible);

-- ============================================
-- FUNCTIONS AND TRIGGERS
-- ============================================

-- Auto-update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cats_updated_at BEFORE UPDATE ON cats
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_health_records_updated_at BEFORE UPDATE ON health_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_appointments_updated_at BEFORE UPDATE ON appointments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_community_posts_updated_at BEFORE UPDATE ON community_posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_lost_found_updated_at BEFORE UPDATE ON lost_found
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_adoption_posts_updated_at BEFORE UPDATE ON adoption_posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Update comments count on community posts
CREATE OR REPLACE FUNCTION update_comments_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE community_posts SET comments_count = comments_count + 1 WHERE id = NEW.post_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE community_posts SET comments_count = comments_count - 1 WHERE id = OLD.post_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_comments_count
AFTER INSERT OR DELETE ON comments
FOR EACH ROW EXECUTE FUNCTION update_comments_count();

-- Update likes count on community posts
CREATE OR REPLACE FUNCTION update_likes_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE community_posts SET likes_count = likes_count + 1 WHERE id = NEW.post_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE community_posts SET likes_count = likes_count - 1 WHERE id = OLD.post_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_likes_count
AFTER INSERT OR DELETE ON post_likes
FOR EACH ROW EXECUTE FUNCTION update_likes_count();
