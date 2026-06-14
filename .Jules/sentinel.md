## 2024-05-24 - Secure SSL Validation
**Vulnerability:** Disabled SSL verification (`verify=False`) in HTTP requests.
**Learning:** Bypassing SSL validation due to occasional bad certificates introduces a trivial downgrade attack (CWE-295).
**Prevention:** Always enforce SSL verification. Handle bad certificates gracefully by catching `requests.exceptions.SSLError` and skipping the resource, instead of falling back to insecure connections.
