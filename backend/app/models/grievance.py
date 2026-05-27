from sqlalchemy import Column, String, Integer, Text, TIMESTAMP, Boolean
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.sql import func
import uuid
from app.core.database import Base

class Grievance(Base):
    __tablename__ = "grievances"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    user_id = Column(UUID(as_uuid=True), nullable=True) # Nullable for anonymous for now
    title = Column(String, index=True)
    description = Column(Text)
    
    # Location (simplified as lat/long columns for SQLAlchemy, PostGIS geometry handled via raw SQL or GeoAlchemy2 if needed)
    # For MVP+ we will stick to WKT or separate lat/long columns for simplicity in python
    lat = Column(String) 
    long = Column(String)
    address_text = Column(String)

    category = Column(String)
    status = Column(String, default="DRAFT") # DRAFT, SUBMITTED, ETC
    
    upvotes = Column(Integer, default=0)
    image_url = Column(String)

    # Index added for performance: ensures O(log N) lookups for keyset pagination on the public grievance feed
    created_at = Column(TIMESTAMP(timezone=True), server_default=func.now(), index=True)
    updated_at = Column(TIMESTAMP(timezone=True), onupdate=func.now())
