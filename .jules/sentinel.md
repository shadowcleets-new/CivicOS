## 2024-05-23 - Overly Permissive CORS Policy
**Vulnerability:** Found `allow_origins=["*"]` configured with `allow_credentials=True` in FastAPI CORS Middleware. This allows any domain to perform authenticated requests and read responses, violating the same-origin policy and leading to potential data exposure.
**Learning:** Browsers generally reject or warn against `*` origin when credentials are allowed. Best practice is to require explicit origins for CORS when credentials are used.
**Prevention:** Always define a strict, configurable list of allowed origins (e.g., in a `Settings` class using environment variables). Default to safe local origins during development, and never use `*` when credentials are permitted.
