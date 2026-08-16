from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import or_, and_
from typing import List
from pydantic import BaseModel
from app.core.database import get_db
from app.models.grievance import Grievance
import uuid

router = APIRouter()

# Schema (Pydantic)
from typing import Optional

class GrievanceCreate(BaseModel):
    title: str
    description: str
    lat: str
    long: str
    category: str
    image_url: Optional[str] = None
    image_url: str | None = None

class GrievanceOut(GrievanceCreate):
    id: uuid.UUID
    status: str
    upvotes: int
    
    class Config:
        from_attributes = True

@router.post("/", response_model=GrievanceOut)
def create_grievance(report: GrievanceCreate, db: Session = Depends(get_db)):
    db_report = Grievance(**report.model_dump())
    db.add(db_report)
    db.commit()
    db.refresh(db_report)
    return db_report

@router.get("/", response_model=List[GrievanceOut])
def read_grievances(limit: int = 100, cursor: uuid.UUID = None, db: Session = Depends(get_db)):
    query = db.query(Grievance).order_by(Grievance.created_at.desc(), Grievance.id.desc())

    if cursor:
        cursor_grievance = db.query(Grievance).filter(Grievance.id == cursor).first()
        if cursor_grievance:
            query = query.filter(
                or_(
                    Grievance.created_at < cursor_grievance.created_at,
                    and_(Grievance.created_at == cursor_grievance.created_at, Grievance.id < cursor_grievance.id)
                )
            )
        else:
            raise HTTPException(status_code=400, detail="Invalid cursor")

    reports = query.limit(limit).all()
    return reports

@router.get("/{report_id}", response_model=GrievanceOut)
def read_grievance(report_id: uuid.UUID, db: Session = Depends(get_db)):
    report = db.query(Grievance).filter(Grievance.id == report_id).first()
    if report is None:
        raise HTTPException(status_code=404, detail="Grievance not found")
    return report

@router.post("/{report_id}/upvote")
def upvote_grievance(report_id: uuid.UUID, db: Session = Depends(get_db)):
    report = db.query(Grievance).filter(Grievance.id == report_id).first()
    if not report:
        raise HTTPException(status_code=404, detail="Not found")
    report.upvotes += 1
    db.commit()
    return {"status": "success", "new_count": report.upvotes}
