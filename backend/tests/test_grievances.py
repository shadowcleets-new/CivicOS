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

def test_read_grievances_keyset_pagination():
    # clear db first
    db = TestingSessionLocal()
    from app.models.grievance import Grievance
    db.query(Grievance).delete()
    db.commit()
    db.close()
    # Insert multiple test grievances
    grievances = []
    # Make sure we use the test app with the test database
    from app.models.grievance import Grievance
    db = TestingSessionLocal()
    import time
    from datetime import datetime, timedelta, timezone

    base_time = datetime.now(timezone.utc)
    for i in range(5):
        db_report = Grievance(
            title=f"Test Grievance {i}",
            description="Test Description",
            lat="0.0",
            long="0.0",
            category="Test Category",
            created_at=base_time + timedelta(seconds=i)
        )
        db.add(db_report)
        db.commit()
        db.refresh(db_report)
        grievances.append(db_report)

        # We need a slight delay to ensure created_at differs slightly if we were relying on it heavily,
        # but the db saves them sequentially and func.now() will likely be identical or slightly different.
        # But we ordered by created_at DESC, id DESC. So even if created_at is the same, id DESC breaks ties.

    db.close()

    # We should have 5 grievances now.
    # Get first page (limit 2)
    response = client.get("/api/v1/grievances/?limit=2")
    assert response.status_code == 200
    page1 = response.json()
    assert len(page1) == 2

    # Get second page using cursor of the last item in page 1
    cursor = page1[-1]['id']
    import time
    time.sleep(0.5)
    response = client.get(f"/api/v1/grievances/?limit=2&cursor={cursor}")
    assert response.status_code == 200
    page2 = response.json()
    assert len(page2) == 2

    # Ensure they are different items
    page1_ids = [item['id'] for item in page1]
    page2_ids = [item['id'] for item in page2]

    assert set(page1_ids).isdisjoint(set(page2_ids))

    # Get third page (should have 1 item)
    cursor = page2[-1]['id']
    response = client.get(f"/api/v1/grievances/?limit=2&cursor={cursor}")
    assert response.status_code == 200
    page3 = response.json()
    assert len(page3) == 1

    # Ensure invalid cursor returns 400
    invalid_cursor = str(uuid.uuid4())
    response = client.get(f"/api/v1/grievances/?limit=2&cursor={invalid_cursor}")
    assert response.status_code == 400
    assert response.json()['detail'] == "Invalid cursor"
