# Security notes

This repository is an educational demo, not a production banking system.

Implemented:
- BCrypt password hashing through Spring Security.
- Role-based endpoint authorization.
- JWT token generation example using a configurable-style secret location in code.
- Transaction locking and rollback on failed operations.
- Audit endpoint protected by ADMIN/AUDITOR roles.

For production, move secrets to a secret manager/environment variables, use TLS,
encrypt sensitive database fields with a managed key, add persistent audit storage,
rotate signing keys, and use a real identity provider.
