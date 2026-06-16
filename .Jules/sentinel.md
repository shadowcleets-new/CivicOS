## 2024-06-16 - Prevent SSL Certificate Validation Bypass
**Vulnerability:** Found `verify=False` in `requests.get` which disabled SSL certificate validation, allowing potential Man-in-the-Middle (MitM) attacks.
**Learning:** Developers disabled verification because some government sites have invalid/expired certificates.
**Prevention:** Always enforce SSL validation. If external sites have invalid certificates, catch `requests.exceptions.SSLError` gracefully rather than globally disabling SSL checks.
