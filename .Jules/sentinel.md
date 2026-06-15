
## 2024-06-15 - Disabled SSL Certificate Validation
**Vulnerability:** The script disabled SSL certificate validation (`verify=False`) when making HTTP requests to download government documents.
**Learning:** Developers often disable SSL validation as a quick workaround for target sites with missing, expired, or improperly configured SSL certificates.
**Prevention:** Never use `verify=False`. Instead, handle `requests.exceptions.SSLError` gracefully to log the failure and skip the resource without crashing the application or introducing security downgrade attacks.
