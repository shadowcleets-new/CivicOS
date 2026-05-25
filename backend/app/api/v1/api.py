from fastapi import APIRouter
from app.api.v1.endpoints import grievances, schemes

api_router = APIRouter()
api_router.include_router(grievances.router, prefix="/grievances", tags=["grievances"])
api_router.include_router(schemes.router, prefix="/schemes", tags=["schemes"])
