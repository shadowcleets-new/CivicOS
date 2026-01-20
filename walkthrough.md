# CivicOS "Super-App" Walkthrough

CivicOS is now a comprehensive Digital Public Infrastructure platform.

## 1. Backend Service (`backend/app/`)
- **Schemes Engine**: `api/v1/endpoints/schemes.py` serves welfare scheme recommendations.
- **Rules Engine**: `jurisdiction_mapper` logic expanded to handle diverse complaints.
- **Database**: `v1_schema.sql` now includes:
    - `schemes`: For welfare data.
    - `laws`: For the Crisis Agent knowledge base.
    - `draft_templates`: for the Letter Writing module.

## 2. Android Native (`android_native/`)
- **Services Tab**: `ServicesScreen.kt` added.
    - Features: Draft RTI, Check Scheme Eligibility.
- **Navigation**: Updated `MainActivity.kt` to include the new tab.

## 3. iOS Native (`ios_native/`)
- **Services Tab**: `ServicesView.swift` added.
    - Features: Legal Drafting, Welfare Finder.
- **Navigation**: Updated `ContentView.swift`.

## 4. How to Run
1.  **Backend**: `uvicorn app.main:app --reload`
    - Check `/docs` to see the new `/schemes` endpoint.
2.  **Android**: Run via Android Studio. You will see the "Services" tab.
3.  **iOS**: Run via Xcode. You will see the "Services" tab.
