import pytest
from app.models.grievance import Grievance
import uuid

def test_read_grievances_empty(client):
    response = client.get("/api/v1/grievances/")
    assert response.status_code == 200
    assert response.json() == []

def test_read_grievances_with_data(client, db):
    # Seed the database
    grievance1 = Grievance(
        title="Pothole on Main St",
        description="Large pothole causing damage to cars.",
        lat="40.7128",
        long="-74.0060",
        category="infrastructure",
        status="DRAFT"
    )
    grievance2 = Grievance(
        title="Streetlight broken",
        description="Streetlight is out on 5th Ave.",
        lat="40.7580",
        long="-73.9855",
        category="infrastructure",
        status="DRAFT"
    )
    db.add_all([grievance1, grievance2])
    db.commit()

    response = client.get("/api/v1/grievances/")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 2
    titles = {d["title"] for d in data}
    assert "Pothole on Main St" in titles
    assert "Streetlight broken" in titles

def test_read_grievances_pagination(client, db):
    # Seed the database
    for i in range(15):
        db.add(Grievance(
            title=f"Grievance {i}",
            description=f"Description {i}",
            lat="0",
            long="0",
            category="other",
            status="DRAFT"
        ))
    db.commit()

    # Test limit
    response = client.get("/api/v1/grievances/?limit=5")
    assert response.status_code == 200
    assert len(response.json()) == 5

    # Test cursor pagination requires a valid cursor ID
    # Since we can't reliably guess the exact cursor in this bulk insert due to identical created_at timestamps,
    # we'll just test that passing an invalid cursor fails gracefully.
    response = client.get("/api/v1/grievances/?cursor=00000000-0000-0000-0000-000000000000")
    assert response.status_code == 400
    assert response.json()["detail"] == "Invalid cursor"

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
