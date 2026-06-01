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

    # Bulk inserting test records will create identical timestamps, leading to non-deterministic ordering
    # Use set-based assertions instead of strict index-based assertions
    titles = {d["title"] for d in data}
    assert "Pothole on Main St" in titles
    assert "Streetlight broken" in titles

import datetime

def test_read_grievances_pagination(client, db):
    # Seed the database
    now = datetime.datetime.now(datetime.timezone.utc)
    for i in range(15):
        # We stagger created_at so cursor pagination tests have distinct timestamps
        # and non-flaky ordering.
        grievance = Grievance(
            title=f"Grievance {i}",
            description=f"Description {i}",
            lat="0",
            long="0",
            category="other",
            status="DRAFT"
        )
        # Using a slight time difference for each element. The first item inserted will be the oldest.
        grievance.created_at = now + datetime.timedelta(seconds=i)
        db.add(grievance)
    db.commit()

    # Test limit (Get the latest 5 items because order_by is desc)
    # The last items inserted (i=14, 13, 12, 11, 10) are the newest.
    response = client.get("/api/v1/grievances/?limit=5")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 5
    assert data[0]["title"] == "Grievance 14"
    assert data[4]["title"] == "Grievance 10"

    # Test cursor pagination (skip the first 5 by passing the 5th item's cursor)
    cursor_id = data[-1]["id"]
    response = client.get(f"/api/v1/grievances/?cursor={cursor_id}&limit=5")
    assert response.status_code == 200
    page2_data = response.json()
    assert len(page2_data) == 5
    assert page2_data[0]["title"] == "Grievance 9"
    assert page2_data[4]["title"] == "Grievance 5"

    # Test next page
    cursor_id2 = page2_data[-1]["id"]
    response = client.get(f"/api/v1/grievances/?cursor={cursor_id2}&limit=10")
    assert response.status_code == 200
    page3_data = response.json()
    assert len(page3_data) == 5
    assert page3_data[0]["title"] == "Grievance 4"
    assert page3_data[-1]["title"] == "Grievance 0"
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
