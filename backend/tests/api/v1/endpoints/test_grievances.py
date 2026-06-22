import pytest
from app.models.grievance import Grievance
import uuid

def test_read_grievances_empty(client):
    response = client.get("/api/v1/grievances/")
    assert response.status_code == 200
    assert response.json() == []

import time
from datetime import datetime, timedelta, timezone

def test_read_grievances_with_data(client, db):
    base_time = datetime.now(timezone.utc)
    # Seed the database
    grievance1 = Grievance(
        title="Pothole on Main St",
        description="Large pothole causing damage to cars.",
        lat="40.7128",
        long="-74.0060",
        category="infrastructure",
        status="DRAFT",
        created_at=base_time
    )
    grievance2 = Grievance(
        title="Streetlight broken",
        description="Streetlight is out on 5th Ave.",
        lat="40.7580",
        long="-73.9855",
        category="infrastructure",
        status="DRAFT",
        created_at=base_time - timedelta(seconds=1)
    )
    db.add_all([grievance1, grievance2])
    db.commit()

    response = client.get("/api/v1/grievances/")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 2
    assert data[0]["title"] == "Pothole on Main St"
    assert data[1]["title"] == "Streetlight broken"

def test_read_grievances_pagination(client, db):
    base_time = datetime.now(timezone.utc)
    # Seed the database
    for i in range(15):
        db.add(Grievance(
            title=f"Grievance {i}",
            description=f"Description {i}",
            lat="0",
            long="0",
            category="other",
            status="DRAFT",
            created_at=base_time - timedelta(seconds=i)
        ))
    db.commit()

    # Test limit
    response = client.get("/api/v1/grievances/?limit=5")
    assert response.status_code == 200
    assert len(response.json()) == 5

    # Test cursor pagination instead of skip
    first_page = response.json()
    last_item_id = first_page[-1]["id"]

    response = client.get(f"/api/v1/grievances/?limit=5&cursor={last_item_id}")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 5
    # The next page should start with the item after the last one on the first page.
    # We inserted Grievance 0 to Grievance 14. 0 has newest time, 14 has oldest time.
    # Page 1: 0, 1, 2, 3, 4 (ordered by created_at desc)
    # Cursor is 4. Page 2 should be: 5, 6, 7, 8, 9
    assert data[0]["title"] == "Grievance 5"

    # Test limit beyond total
    last_item_id = data[-1]["id"]
    response = client.get(f"/api/v1/grievances/?limit=10&cursor={last_item_id}")
    assert response.status_code == 200
    assert len(response.json()) == 5
from fastapi.testclient import TestClient

def test_create_grievance(client: TestClient):
    # Test data
    grievance_data = {
        "title": "Pothole on Main St",
        "description": "Large pothole causing traffic issues.",
        "lat": "40.7128",
        "long": "-74.0060",
        "category": "road_damage",
        "image_url": "http://example.com/image.jpg"
    }

    # Send POST request
    response = client.post(
        "/api/v1/grievances/",
        json=grievance_data,
    )

    # Assert response
    assert response.status_code == 200
    data = response.json()
    assert data["title"] == grievance_data["title"]
    assert data["description"] == grievance_data["description"]
    assert data["lat"] == grievance_data["lat"]
    assert data["long"] == grievance_data["long"]
    assert data["category"] == grievance_data["category"]
    assert data["image_url"] == grievance_data["image_url"]
    assert "id" in data
    assert data["status"] == "DRAFT"
    assert data["upvotes"] == 0

def test_create_grievance_missing_fields(client: TestClient):
    # Test data missing required fields
    grievance_data = {
        "title": "Pothole on Main St",
        # Missing description, lat, long, category
    }

    # Send POST request
    response = client.post(
        "/api/v1/grievances/",
        json=grievance_data,
    )

    # Assert response is 422 Unprocessable Entity
    assert response.status_code == 422
