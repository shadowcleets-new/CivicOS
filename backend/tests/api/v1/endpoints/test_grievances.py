import uuid

def test_upvote_grievance_not_found(client):
    """
    Test upvoting a non-existent grievance to ensure it returns a 404 status code.
    """
    dummy_uuid = uuid.uuid4()
    response = client.post(f"/api/v1/grievances/{dummy_uuid}/upvote")
    assert response.status_code == 404
    assert response.json()["detail"] == "Not found"
