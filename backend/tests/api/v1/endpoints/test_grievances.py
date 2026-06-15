import time
from datetime import timedelta
import datetime

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
    base_time = datetime.datetime.now(datetime.timezone.utc)
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
    response1 = client.get("/api/v1/grievances/?limit=5")
    assert response1.status_code == 200
    data1 = response1.json()
    assert len(data1) == 5
    # Since we explicitly staggered creation time, we can verify sorting order
    assert data1[0]["title"] == "Grievance 0"

    # Test cursor pagination correctly
    last_id_1 = data1[-1]["id"]
    response2 = client.get(f"/api/v1/grievances/?cursor={last_id_1}&limit=5")
    assert response2.status_code == 200
    data2 = response2.json()
    assert len(data2) == 5
    assert data2[0]["title"] == "Grievance 5"

    # Test cursor pagination correctly beyond total
    last_id_2 = data2[-1]["id"]
    response3 = client.get(f"/api/v1/grievances/?cursor={last_id_2}&limit=10")
    assert response3.status_code == 200
    data3 = response3.json()
    assert len(data3) == 5
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
