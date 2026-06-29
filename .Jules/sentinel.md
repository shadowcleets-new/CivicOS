## 2026-06-29 - Enforce SSL Certificate Validation
**Vulnerability:** Call to requests with `verify=False` disabling SSL certificate checks, creating a security issue (B501).
**Learning:** Gov sites often have bad certs, but disabling SSL check entirely introduces severe downgrade attack risks.
**Prevention:** Always enforce SSL validation (`verify=True`). Catch `requests.exceptions.SSLError` to handle specific bad cert failures gracefully without globally disabling verification.
