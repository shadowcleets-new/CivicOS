## 2026-07-16 - Enforce SSL Certificate Validation
**Vulnerability:** Found `verify=False` in `requests.get` within `backend/scripts/crisis_agent_setup.py` which disables SSL certificate validation.
**Learning:** Disabling SSL validation (`verify=False`) makes the application vulnerable to Man-in-the-Middle (MitM) attacks. We must never fall back to `verify=False` even if some gov sites have bad certs.
**Prevention:** Always enforce SSL validation. Handle `requests.exceptions.SSLError` gracefully by logging the failure and skipping the resource rather than bypassing the check.
