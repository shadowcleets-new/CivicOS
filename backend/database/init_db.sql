-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Create tables (Schema for imported GeoJSONs)
CREATE TABLE IF NOT EXISTS roads (
    id SERIAL PRIMARY KEY,
    osm_id BIGINT,
    name VARCHAR(255),
    ref VARCHAR(255),
    highway VARCHAR(50),
    jurisdiction VARCHAR(100),
    geom GEOMETRY(LineString, 4326)
);

CREATE TABLE IF NOT EXISTS admin_boundaries (
    id SERIAL PRIMARY KEY,
    osm_id BIGINT,
    name VARCHAR(255),
    admin_level INT,
    geom GEOMETRY(Polygon, 4326) -- or MultiPolygon
);

-- Indexing for performance
CREATE INDEX IF NOT EXISTS idx_roads_geom ON roads USING GIST (geom);
CREATE INDEX IF NOT EXISTS idx_boundaries_geom ON admin_boundaries USING GIST (geom);


-- Function: get_authority_by_lat_long
-- Logic: Buffer point -> Check Road Intersection -> Check Admin Boundary
CREATE OR REPLACE FUNCTION get_authority_by_lat_long(lat DOUBLE PRECISION, lon DOUBLE PRECISION)
RETURNS TABLE (authority_type VARCHAR, authority_name VARCHAR, details TEXT) AS $$
DECLARE
    user_location GEOMETRY;
    road_record RECORD;
    boundary_record RECORD;
BEGIN
    -- Create Point Geometry (SRID 4326)
    user_location := ST_SetSRID(ST_MakePoint(lon, lat), 4326);
    
    -- 1. Check for Road Intersection (Buffer 10m approx 0.0001 degrees, but better to use Geography or transform to UTM)
    -- Using simple caste to geography for meter buffering
    SELECT * INTO road_record
    FROM roads
    WHERE ST_DWithin(user_location::geography, geom::geography, 10) -- 10 meters
    ORDER BY ST_Distance(user_location::geography, geom::geography) ASC
    LIMIT 1;
    
    IF FOUND THEN
        authority_type := 'Road Authority';
        authority_name := road_record.jurisdiction;
        details := 'Identified road: ' || COALESCE(road_record.name, 'Unnamed') || ' (' || COALESCE(road_record.ref, '') || ')';
        RETURN NEXT;
        RETURN;
    END IF;

    -- 2. Check for Administrative Polygons (Ward/District)
    SELECT * INTO boundary_record
    FROM admin_boundaries
    WHERE ST_Intersects(user_location, geom)
    ORDER BY admin_level DESC -- Prefer more granular (higher level) e.g., Ward over District
    LIMIT 1;

    IF FOUND THEN
        authority_type := 'Civic Authority';
        authority_name := 'Municipal Corporation'; -- Default bucket
        details := 'Inside Boundary: ' || boundary_record.name || ' (Level ' || boundary_record.admin_level || ')';
        RETURN NEXT;
        RETURN;
    END IF;

    -- 3. Fallback
    authority_type := 'Unknown';
    authority_name := 'Unmapped Territory';
    details := 'No specific jurisdiction found.';
    RETURN NEXT;
END;
$$ LANGUAGE plpgsql;

-- Table: Officials (Crawled Data)
CREATE TABLE IF NOT EXISTS officials (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    designation VARCHAR(255),
    phone VARCHAR(50),
    department VARCHAR(255),
    source_url VARCHAR(500),
    confidence_score INT DEFAULT 50, -- Starts at 50, max 100, min 0
    last_verified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Index for phone search
CREATE INDEX IF NOT EXISTS idx_officials_phone ON officials(phone);

