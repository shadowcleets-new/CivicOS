
## 2024-05-24 - Enforce Strict SSL Validation
**Vulnerability:** The script `backend/scripts/crisis_agent_setup.py` disabled SSL certificate validation (`verify=False`) when making HTTP requests to download files.
**Learning:** Disabling SSL verification exposes the application to man-in-the-middle (MITM) attacks and compromises the integrity of downloaded data. It should never be used, even if target servers have configuration issues; such errors must be handled gracefully instead.
**Prevention:** Never use `verify=False` in HTTP requests. If SSL errors occur, handle the exception gracefully, log the error, and fail securely without compromising validation.
