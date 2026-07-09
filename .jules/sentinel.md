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

## 2024-07-09 - Disabled SSL Verification in Requests
**Vulnerability:** The `requests.get` call in `crisis_agent_setup.py` was made with `verify=False`, which disables SSL certificate verification and allows Man-in-the-Middle (MitM) attacks.
**Learning:** Attempting to bypass SSL verification errors by setting `verify=False` introduces a serious vulnerability.
**Prevention:** Always enforce SSL validation for outbound network requests. When encountering bad certificates that cannot be resolved, use a try-except block to gracefully catch `requests.exceptions.SSLError` and skip the operation securely.
