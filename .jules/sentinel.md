## 2024-05-23 - Hardcoded Secret Key
**Vulnerability:** Hardcoded `SECRET_KEY` with a default value in Pydantic Settings class.
**Learning:** Hardcoded secrets in code pose a high security risk as they can be easily leaked or committed to version control.
**Prevention:** Make sensitive configuration values required fields in Pydantic Settings by removing default values. This forces the application to load them securely from environment variables or `.env` files and prevents the app from starting with insecure defaults.

## 2024-05-25 - [Fix Hardcoded Secrets in Config]
**Vulnerability:** Hardcoded PostgreSQL password and an empty Google API key with default values in the `backend/app/core/config.py` settings file.
**Learning:** Default secrets in Pydantic `BaseSettings` configurations bypass environment variable injection and create a critical security vulnerability by exposing passwords directly in the codebase.
**Prevention:** Never provide default values for sensitive configuration items like passwords or API keys. Always rely on explicit type declarations (e.g., `POSTGRES_PASSWORD: str`) to force injection via environment variables or a `.env` file, ensuring "fail securely" principles are met.
