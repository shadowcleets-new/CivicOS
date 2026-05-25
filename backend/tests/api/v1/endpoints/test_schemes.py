import pytest
from fastapi.testclient import TestClient

def test_recommend_schemes_student_female(client: TestClient):
    response = client.get("/api/v1/schemes/recommend?gender=Female&occupation=Student")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 3
    titles = [item["title"] for item in data]
    assert "Mahila Shakti Kendra" in titles
    assert "Post Matric Scholarship" in titles
    assert "Ayushman Bharat" in titles

def test_recommend_schemes_male_student(client: TestClient):
    response = client.get("/api/v1/schemes/recommend?gender=Male&occupation=Student")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 2
    titles = [item["title"] for item in data]
    assert "Post Matric Scholarship" in titles
    assert "Ayushman Bharat" in titles
    assert "Mahila Shakti Kendra" not in titles

def test_recommend_schemes_no_params(client: TestClient):
    response = client.get("/api/v1/schemes/recommend")
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 1
    titles = [item["title"] for item in data]
    assert "Ayushman Bharat" in titles
