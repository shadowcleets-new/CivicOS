from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import update
from sqlalchemy.orm import Session
from typing import List
from pydantic import BaseModel
from app.core.database import get_db
from app.models.grievance import Grievance
import uuid

router = APIRouter()

# Schema (Pydantic)
class GrievanceCreate(BaseModel):
    title: str
    description: str
    lat: str
    long: str
    category: str
    image_url: str = None

class GrievanceOut(GrievanceCreate):
    id: uuid.UUID
    status: str
    upvotes: int
    
    class Config:
        from_attributes = True

@router.post("/", response_model=GrievanceOut)
def create_grievance(report: GrievanceCreate, db: Session = Depends(get_db)):
    db_report = Grievance(**report.dict())
    db.add(db_report)
    db.commit()
    db.refresh(db_report)
    return db_report

@router.get("/", response_model=List[GrievanceOut])
def read_grievances(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    reports = db.query(Grievance).offset(skip).limit(limit).all()
    return reports

@router.get("/{report_id}", response_model=GrievanceOut)
def read_grievance(report_id: uuid.UUID, db: Session = Depends(get_db)):
    report = db.query(Grievance).filter(Grievance.id == report_id).first()
    if report is None:
        raise HTTPException(status_code=404, detail="Grievance not found")
    return report

@router.post("/{report_id}/upvote")
def upvote_grievance(report_id: uuid.UUID, db: Session = Depends(get_db)):
    # ⚡ Bolt: Use atomic update for upvotes to prevent race conditions and halve DB roundtrips
    stmt = (
        update(Grievance)
        .where(Grievance.id == report_id)
        .values(upvotes=Grievance.upvotes + 1)
        .returning(Grievance.upvotes)
    )
    result = db.execute(stmt)
    new_count = result.scalar()

    if new_count is None:
        raise HTTPException(status_code=404, detail="Not found")

    db.commit()
    return {"status": "success", "new_count": new_count}
