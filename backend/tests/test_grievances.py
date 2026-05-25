from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import uuid
import sys
import os

# Add the backend directory to the path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app.api.v1.endpoints.grievances import router as grievances_router
from app.core.database import get_db, Base
from fastapi import FastAPI

# Create a clean app specifically for testing the router
app = FastAPI()
app.include_router(grievances_router, prefix="/api/v1/grievances")

# Use an in-memory SQLite database for testing
SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"
engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False})
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db

client = TestClient(app)

# Setup tables
Base.metadata.create_all(bind=engine)

def test_read_grievance_not_found():
    non_existent_id = uuid.uuid4()
    response = client.get(f"/api/v1/grievances/{non_existent_id}")
    assert response.status_code == 404
    assert response.json() == {"detail": "Grievance not found"}
