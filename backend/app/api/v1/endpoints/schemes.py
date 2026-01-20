from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from pydantic import BaseModel
from app.core.database import get_db
# from app.models.scheme import Scheme # Assumed model exists

router = APIRouter()

class SchemeOut(BaseModel):
    id: int
    title: str
    ministry: str
    beneficiary_type: str = "General"

@router.get("/recommend", response_model=List[SchemeOut])
def recommend_schemes(
    age: int = Query(None),
    gender: str = Query(None),
    income: int = Query(None),
    occupation: str = Query(None),
    db: Session = Depends(get_db)
):
    """
    Returns schemes based on user profile.
    (Mock Logic for MVP - Real logic uses checking criteria in DB)
    """
    # Mock Response
    recommendations = []
    
    if gender == "Female":
         recommendations.append(SchemeOut(id=1, title="Mahila Shakti Kendra", ministry="WCD"))
    
    if occupation == "Student":
        recommendations.append(SchemeOut(id=2, title="Post Matric Scholarship", ministry="Education"))
        
    recommendations.append(SchemeOut(id=3, title="Ayushman Bharat", ministry="Health"))
        
    return recommendations
