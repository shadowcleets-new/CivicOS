## 2024-06-30 - SSL Certificate Validation
**Vulnerability:** `verify=False` was used in `requests.get`, disabling SSL certificate validation.
**Learning:** Hardcoding `verify=False` enables MITM attacks. It's often added as a hack for sites with bad certificates, but this is a critical security vulnerability.
**Prevention:** Ensure network calls enforce SSL validation. Handle certificate errors gracefully using try-except for `requests.exceptions.SSLError` rather than disabling validation globally or implementing a fallback to `verify=False`.
