-- CivicOS V1 Full Schema
-- Extends the initial PostGIS setup

-- 1. USERS & AUTH
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100),
    home_lat DOUBLE PRECISION,
    home_long DOUBLE PRECISION,
    karma_points INT DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE, -- Via Aadhaar/OTP
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 2. DEPARTMENTS (Static/Scraped Master Data)
CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, -- e.g., "BBMP Road Dept", "Bescom"
    type VARCHAR(50), -- "Municipal", "State", "Central"
    jurisdiction_level INT -- 1=Local, 2=City, 3=State
);

-- 3. JURISDICTION ZONES (Linked to PostGIS Geom)
-- This links our mapped polygons to departments
CREATE TABLE IF NOT EXISTS jurisdiction_map (
    id SERIAL PRIMARY KEY,
    department_id INT REFERENCES departments(id),
    boundary_geom GEOMETRY(Polygon, 4326),
    ward_number VARCHAR(20),
    zone_name VARCHAR(100)
);

-- 4. GRIEVANCES (The Core Ticket)
CREATE TYPE report_status AS ENUM ('DRAFT', 'SUBMITTED', 'VERIFIED_BY_AI', 'SENT_TO_OFFICIAL', 'RESOLVED', 'REJECTED');

CREATE TABLE IF NOT EXISTS grievances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    title VARCHAR(200),
    description TEXT,
    location_geom GEOMETRY(Point, 4326),
    address_text TEXT,
    
    -- AI Classification
    category VARCHAR(50), -- "Pothole", "Garbage", "Streetlight"
    severity_score INT, -- 1-10
    
    -- Routing
    assigned_dept_id INT REFERENCES departments(id),
    assigned_official_id INT REFERENCES officials(id), -- From our scraper
    
    -- Media
    image_url TEXT,
    video_url TEXT,
    
    status report_status DEFAULT 'DRAFT',
    
    -- Gamification
    upvotes INT DEFAULT 0,
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 5. COMMUNITY INTERACTION
CREATE TABLE IF NOT EXISTS votes (
    user_id UUID REFERENCES users(id),
    grievance_id UUID REFERENCES grievances(id),
    vote_type INT, -- 1 or -1
    PRIMARY KEY (user_id, grievance_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    grievance_id UUID REFERENCES grievances(id),
    content TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 6. AI AUDIT LOG (Transparency Layer)
CREATE TABLE IF NOT EXISTS ai_audit_logs (
    id SERIAL PRIMARY KEY,
    grievance_id UUID REFERENCES grievances(id),
    action_type VARCHAR(50), -- "CLASSIFICATION", "DRAFTING", "ROUTING"
    input_context TEXT,
    model_output TEXT,
    confidence_score FLOAT,
-- ... (Previous Tables) ...

-- 7. SCHEMES & BENEFITS (New Module)
CREATE TABLE IF NOT EXISTS schemes (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    ministry VARCHAR(255),
    description TEXT,
    eligibility_criteria TEXT, -- JSON or Text description
    benefits TEXT,
    tags VARCHAR[], -- ['education', 'women', 'business']
    link_url VARCHAR(500)
);

-- 8. LEGAL KNOWLEDGE BASE (For the Agent)
CREATE TABLE IF NOT EXISTS laws (
    id SERIAL PRIMARY KEY,
    act_name VARCHAR(255), -- "Motor Vehicles Act", "Consumer Protection Act"
    section VARCHAR(50),
    description TEXT,
    penalty TEXT,
    embedding vector(768) -- For semantic search (pgvector extension required)
);

-- 9. DRAFTING TEMPLATES
CREATE TABLE IF NOT EXISTS draft_templates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100), -- "Gas Connection Transfer", "RTI"
    template_text TEXT, -- Jinja2 format
    required_fields VARCHAR[] -- ['user_name', 'consumer_number']
);

-- 10. EXPANDED JURISDICTION RULES
-- Maps categories to specific department types logic
CREATE TABLE IF NOT EXISTS jurisdiction_rules (
    category VARCHAR(50) PRIMARY KEY, -- "Ecological", "Railway", "Pothole"
    logic_type VARCHAR(50), -- "POLYGON", "NEAREST_POI", "STATE_LEVEL"
    target_dept_type VARCHAR(100) -- "Forest Dept", "Railway Police", "Municipal"
);

