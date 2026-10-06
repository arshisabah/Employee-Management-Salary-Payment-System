# API testing guide

Base URL: `http://localhost:8082`. Seed data (created on first start): employer `1`, employees `1` Rahul (active), `2` Priya (active), `3` Amit (inactive), and 3 transactions dated this month.

Even employee IDs always end as FAILED and odd IDs as SUCCESS (payment simulation).

## Employer

| Method | URL | Body |
|---|---|---|
| POST | `/api/employers` | see below |
| GET | `/api/employers/{id}` | none |
| GET | `/api/employers/{id}/employees` | none |
| GET | `/api/employers/{id}/employees/search?name=rahul` | none |
| GET | `/api/employers/{id}/transactions` | none |

```json
{ "name": "Gulf Star Trading", "arabicName": "نجمة الخليج للتجارة", "registrationMonth": "October", "debitCardNumber": "4111111111111111" }
```

Response masks the card: `"debitCardNumber": "************1111"`.

## Employee

| Method | URL | Body |
|---|---|---|
| POST | `/api/employers/{employerId}/employees` | see below |
| GET | `/api/employees/{id}` | none |
| PUT | `/api/employees/{id}/deregister` | none |
| GET | `/api/employees/{id}/transactions` | none |

```json
{ "name": "Sara Ahmed", "creditAccountNumber": "9876543299", "monthSalaryAmount": 42000.50 }
```

## Transactions

| Method | URL |
|---|---|
| POST | `/api/transactions/pay` |
| GET | `/api/transactions` |
| GET | `/api/transactions?date=2026-10-06` |
| GET | `/api/transactions?from=2026-10-01&to=2026-10-06` |

```json
{ "employerId": 1, "employeeId": 4 }
```

## Test sequence (Postman order)

1. `POST /api/employers` with the Gulf Star body, so you get employer `2`.
2. `POST /api/employers/2/employees` with Sara, so you get employee `4`. Register a second one, Omar (`"creditAccountNumber": "9876543300"`, `"monthSalaryAmount": 30000`), to get employee `5`.
3. `GET /api/employers/2/employees`, expect 2 rows.
4. `GET /api/employers/2/employees/search?name=SARA`, expect 1 row (case-insensitive).
5. `POST /api/transactions/pay` with `{ "employerId": 2, "employeeId": 4 }`, expect 201 and FAILED (even ID). Pay employee `5`, expect SUCCESS.
6. Repeat the pay call for employee `5`, expect 400 `Salary has already been requested for this month`. Employee `4` can retry because FAILED payments don't count.
7. `PUT /api/employees/5/deregister`, then pay again, expect 400 `Employee is not active`.
8. `GET /api/transactions`, newest first.

## Error cases to try

| Request | Expected |
|---|---|
| `GET /api/employers/999` | 404 `Employer not found` |
| `GET /api/employees/999` | 404 `Employee not found` |
| pay `{ "employerId": 1, "employeeId": 4 }` (4 belongs to employer 2) | 400 `Employee does not belong to this employer` |
| pay `{ "employerId": 1, "employeeId": 3 }` | 400 `Employee is not active` |
| `POST /api/employers` with `"arabicName": ""` | 400 `Employer Arabic name is required` |
| register employee with `"monthSalaryAmount": 0` | 400 `Month salary amount must be greater than zero` |
| register employee with `"monthSalaryAmount": 99999999999` | 400 `...at most 10 digits and 2 decimal places` |
| `GET /api/transactions?date=abc` | 400 `Invalid 'date' date. Use format yyyy-MM-dd` |
| `GET /api/transactions?from=2026-10-01` | 400 `Both 'from' and 'to' dates are required for a date range` |
| `GET /api/transactions?from=2026-10-06&to=2026-10-01` | 400 `'from' date must not be after 'to' date` |

## Arabic text

Create the database as UTF-8 so Arabic is not stored as `????`:

```sql
CREATE DATABASE employee_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

If the `employer` table already exists, Hibernate adds the new `arabic_name` column on startup (`ddl-auto=update`). Old rows keep it empty (`null`). To give an existing employer a value, run `UPDATE employer SET arabic_name = 'إيه بي سي للتقنية' WHERE id = 1;`.

## Frontend

Start the app and open `http://localhost:8082/`. The page is `src/main/resources/static/index.html`, so it needs no extra setup.
