from fastapi import FastAPI
from pydantic import BaseModel
import uvicorn
import os

app = FastAPI(title="CivicOS Backend", version="MVP-0.1")

@app.get("/")
def read_root():
    return {"status": "online", "system": "CivicOS-MVP"}

@app.get("/health")
def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
