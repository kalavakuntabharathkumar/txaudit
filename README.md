# Secure Transaction Processing & Audit System

A demo transaction platform with a Java transactional API, Python batch
reconciliation service, JWT authentication, role-based access control,
audit records, rollback handling, and tests.

## Run
Java service:
```bash
cd java-service
mvn spring-boot:run
```
Python reconciliation service:
```bash
cd python-reconciliation
pip install -r requirements.txt
uvicorn app:app --reload --port 8002
```

Demo credentials are intentionally non-production examples:
- admin / admin123
- auditor / audit123

JWTs are signed with `JWT_SECRET`; replace it in real deployments.

## Test
```bash
cd java-service
mvn test
```

The Python service includes a 5,000-record reconciliation benchmark generator.
