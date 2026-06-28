
## 2024-05-23 - Enforce SSL Certificate Validation
**Vulnerability:** Found `verify=False` in `requests.get` which disables SSL certificate checks, allowing man-in-the-middle attacks (CWE-295).
**Learning:** Do not bypass SSL verification, even for sites with known bad certificates, as falling back to `verify=False` introduces a trivial downgrade attack.
**Prevention:** Remove `verify=False` and handle `requests.exceptions.SSLError` gracefully to log the failure and skip the resource without crashing the application.
