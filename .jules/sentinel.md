## 2024-05-23 - Hardcoded Secret Key
**Vulnerability:** Hardcoded `SECRET_KEY` with a default value in Pydantic Settings class.
**Learning:** Hardcoded secrets in code pose a high security risk as they can be easily leaked or committed to version control.
**Prevention:** Make sensitive configuration values required fields in Pydantic Settings by removing default values. This forces the application to load them securely from environment variables or `.env` files and prevents the app from starting with insecure defaults.
