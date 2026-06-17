
## 2024-06-17 - Enforce SSL Certificate Validation in Requests
**Vulnerability:** Call to `requests.get` with `verify=False` disabling SSL certificate checks.
**Learning:** Using `verify=False` opens the application to man-in-the-middle (MITM) attacks and compromises the integrity of downloaded files.
**Prevention:** Always enforce SSL validation by leaving `verify` as its default (`True`), and handle `requests.exceptions.SSLError` gracefully to skip insecure resources instead of disabling validation.
