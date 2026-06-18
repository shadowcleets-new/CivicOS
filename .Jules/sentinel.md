
## 2024-06-18 - Fix SSL Certificate Validation Vulnerability
**Vulnerability:** Disabled SSL certificate validation (`verify=False`) in `requests.get` inside `backend/scripts/crisis_agent_setup.py`.
**Learning:** Endpoints with bad certs often lead developers to disable SSL verification (`verify=False`) as a quick workaround, which opens the application to Man-in-the-Middle (MitM) attacks.
**Prevention:** Always enforce SSL validation. Wrap network requests in a try-except block catching `requests.exceptions.SSLError` to gracefully handle bad certificates by logging and skipping the resource without crashing or compromising security.
