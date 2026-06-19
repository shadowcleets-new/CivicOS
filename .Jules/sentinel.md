
## 2024-05-24 - Enforce SSL Validation Without Fallback
**Vulnerability:** Disabling SSL certificate validation globally (`verify=False`) in requests.
**Learning:** Government or external sites may have bad certificates, leading developers to disable validation entirely. Catching SSLError to fallback to `verify=False` introduces a downgrade attack.
**Prevention:** Always enforce SSL validation. Handle `requests.exceptions.SSLError` gracefully to log and skip failed resources instead of bypassing security checks.
