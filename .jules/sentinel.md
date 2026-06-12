## 2024-05-23 - Hardcoded Secret Key
**Vulnerability:** Hardcoded `SECRET_KEY` with a default value in Pydantic Settings class.
**Learning:** Hardcoded secrets in code pose a high security risk as they can be easily leaked or committed to version control.
**Prevention:** Make sensitive configuration values required fields in Pydantic Settings by removing default values. This forces the application to load them securely from environment variables or `.env` files and prevents the app from starting with insecure defaults.

## 2024-05-24 - Hardcoded Database Credentials
**Vulnerability:** Hardcoded `POSTGRES_PASSWORD` and default `GOOGLE_API_KEY` in Pydantic Settings class.
**Learning:** Hardcoded database credentials or API keys pose a critical security risk and can lead to unauthorized access if committed to version control.
**Prevention:** Make sensitive configuration values required fields in Pydantic Settings by removing default values. This forces the application to load them securely from environment variables or `.env` files.
## 2024-05-23 - Hardcoded Database Credentials in Pydantic Config
**Vulnerability:** The `POSTGRES_PASSWORD` was hardcoded to a default value in the `Settings` class (`backend/app/core/config.py`).
**Learning:** Hardcoding credentials in source code exposes them to anyone with repository access. Even if intended for local development, it can leak to production.
**Prevention:** Rely on `pydantic_settings` to inject secrets via environment variables by defining the variable type without providing a default value.

## 2024-06-12 - Disabled SSL Certificate Validation
**Vulnerability:** The `requests.get()` call used to download PDF files was configured with `verify=False`, which disables SSL certificate verification.
**Learning:** Disabling SSL verification introduces a severe vulnerability to Man-in-the-Middle (MITM) attacks, making it possible for attackers to intercept and alter downloaded documents (like legal guidelines) without detection. This is particularly dangerous for an agent that ingests this data as truth.
**Prevention:** Never use `verify=False` to bypass SSL certificate errors in production or critical scripts. Instead, proper certificates should be installed or requests should fail securely. Never implement bypassable try-catch blocks for SSL verification (e.g., attempting `verify=True` and falling back to `verify=False` on `SSLError`).
