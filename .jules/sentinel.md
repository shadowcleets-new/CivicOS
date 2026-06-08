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

## 2024-05-24 - Disabled SSL Certificate Validation
**Vulnerability:** Call to requests with `verify=False` in `backend/scripts/crisis_agent_setup.py` disabled SSL certificate checks.
**Learning:** Disabling SSL verification (`verify=False`) to bypass misconfigured external servers allows Man-in-the-Middle (MITM) attacks. Never disable TLS/SSL validation, even as a workaround.
**Prevention:** Enforce strict TLS/SSL certificate validation across the codebase by ensuring `verify=True` (or leaving it default) for all network requests.

## 2024-05-24 - Disabled SSL Certificate Validation handling
**Vulnerability:** Call to requests with `verify=False` in `backend/scripts/crisis_agent_setup.py` disabled SSL certificate checks globally for the script, exposing it to MITM attacks.
**Learning:** Hardcoding `verify=False` to bypass known bad certificates on specific external servers undermines security for all potential requests. Simply changing it to `verify=True` can break functionality if the external server genuinely has issues.
**Prevention:** Implement a secure fallback pattern: always attempt strict TLS/SSL certificate validation (`verify=True`) first. If it fails with an `SSLError`, catch the specific exception, log a clear warning, and only then fall back to `verify=False` for that specific, known problematic request.
