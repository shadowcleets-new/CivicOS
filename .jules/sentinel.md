## 2026-05-23 - Overly Permissive CORS Configuration
**Vulnerability:** The FastAPI backend had overly permissive CORS configured (`allow_origins=["*"]`) alongside `allow_credentials=True`. This is a severe security misconfiguration that can lead to Cross-Origin Resource Sharing attacks, allowing unauthorized third-party domains to make authenticated requests.
**Learning:** Using `["*"]` for `allow_origins` while `allow_credentials=True` is insecure and violates the CORS specification. It's a common oversight during development.
**Prevention:** Always restrict `allow_origins` to a defined list of trusted origins (e.g., loaded from a configuration file) instead of allowing all origins, especially when dealing with authenticated sessions (`allow_credentials=True`).
