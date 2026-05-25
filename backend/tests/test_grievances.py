import pytest
import uuid
from unittest.mock import MagicMock
from fastapi import HTTPException
from sqlalchemy.orm import Session
from app.api.v1.endpoints.grievances import upvote_grievance

def test_upvote_grievance_not_found():
    # Arrange
    mock_db = MagicMock(spec=Session)
    mock_query = mock_db.query.return_value
    mock_filter = mock_query.filter.return_value
    mock_filter.first.return_value = None

    dummy_uuid = uuid.uuid4()

    # Act & Assert
    with pytest.raises(HTTPException) as exc_info:
        upvote_grievance(report_id=dummy_uuid, db=mock_db)

    assert exc_info.value.status_code == 404
    assert exc_info.value.detail == "Not found"
