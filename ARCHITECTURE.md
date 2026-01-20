# CivicOS - Architecture & Stack Definition

## 1. Overview
CivicOS is a Digital Public Infrastructure (DPI) "Super-App" designed to bridge the gap between Indian citizens and the State. It leverages OSINT, geospatial inference, and LLM-based reasoning to solve jurisdictional ambiguity and automate bureaucratic navigation.

## 2. Technology Stack

### Frontend (Mobile)
- **Framework**: **Flutter**
- **Justification**:
    - **Cross-Platform**: Single codebase for both Android and iOS, crucial for wide reach in India.
    - **Offline-First**: robust local storage and logic capabilities are essential for areas with spotty network connectivity (Tier-2/3 cities, rural areas).
    - **Performance**: Skia engine ensures smooth performance even on low-end budget devices common in the target demographic.

### Backend & Logic
- **Language/Framework**: **Python (FastAPI)**
- **Justification**:
    - **Geospatial Ecosystem**: Python has the best-in-class libraries for geospatial data manipulation (GeoPandas, Shapely, Pyrosm).
    - **AI Integration**: Native support for LangChain, Gemini API, and Vector DB clients.
    - **Async**: FastAPI provides high-performance asynchronous capabilities needed for handling concurrent user requests.

### Database
- **Primary DB**: **PostgreSQL + PostGIS**
- **Justification**:
    - **Spatial Queries**: The absolute standard for complex geospatial queries ("Point-in-Polygon") required to determine "Who owns this road?" (NHAI vs. Municipal Corporation).
    - **Reliability**: ACID compliance for critical civic data.

### AI/LLM Layer
- **Inference Engine**: **Gemini 2.0 Flash** (via Antigravity/Google GenAI SDK)
    - **Role**: Reasoning engine for "Crisis Mode", drafting legal documents, and parsing complex government resolutions.
- **Vector Database**: **ChromaDB**
    - **Role**: RAG (Retrieval-Augmented Generation) storage for Act chunks, Government Resolutions (GRs), and legal guidelines.

### Scraping Infrastructure
- **Tools**: **Playwright (Python)** + **Tesseract OCR**
    - **Justification**:
        - **Dynamic Content**: Many government portals are legacy ASP.NET or Java sites with heavy AJAX, requiring a full browser engine (Playwright).
        - **CAPTCHA**: Local Tesseract OCR is needed to solve simple alphanumeric CAPTCHAs found on directory pages without external API costs.

## 3. Key Modules

### Jurisdiction Engine
- **Core Logic**: Hierarchical spatial checks (buffer -> road vector -> administrative polygon).
- **Data Source**: OpenStreetMap (OSM) vector data, enhanced with scraped directory info.

### Crisis & Justice Agent
- **Function**: RAG-based legal advice and safety protocols.
- **Safety**: "Golden Hour" protocol prioritization.

### Bureaucratic Crawler
- **Target**: `*.nic.in`, `*.gov.in`.
- **Strategy**: Pattern matching for "Contacts" pages, regex extraction of official numbers.

## 4. Security & Privacy
- **PII Vault**: All user PII (Aadhaar, Phone) stored encrypted in local SQLite on the device.
- **Ethics**: Rate-limited scraping (1 req/5s), respecting robots.txt where feasible.
