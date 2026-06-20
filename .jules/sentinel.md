## 2024-06-20 - Enforce SSL Certificate Validation
**Vulnerability:** SSL certificate validation was disabled (`verify=False`) in `requests.get` to bypass bad certificates on government websites.
**Learning:** Disabling SSL validation (`verify=False`) allows man-in-the-middle (MITM) attacks and introduces a trivial downgrade attack, rendering the connection insecure. It should never be used, even as a fallback for `SSLError`.
**Prevention:** Always enforce SSL validation. Catch `requests.exceptions.SSLError` to gracefully handle bad certificates by logging the failure and skipping the resource.
