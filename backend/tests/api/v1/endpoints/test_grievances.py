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
    assert data[0]["title"] == "Pothole on Main St"
    assert data[1]["title"] == "Streetlight broken"

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

    # Test skip
    response = client.get("/api/v1/grievances/?skip=5&limit=5")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 5
    assert data[0]["title"] == "Grievance 5"

    # Test skip and limit beyond total
    response = client.get("/api/v1/grievances/?skip=10&limit=10")
    assert response.status_code == 200
    assert len(response.json()) == 5
