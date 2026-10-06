# Employee Management & Salary Payment System

This is a beginner-friendly Spring Boot project for managing employees and processing one-time monthly salary payments.

## Features

- Create an employer
- Register employees under an employer
- Search employees by name
- Deregister an employee without deleting the record
- Pay salary once per month
- View transactions ordered by newest first
- Support date and date-range filtering
- Simulate a payment result as SUCCESS or FAILED
- Return masked debit card numbers in API responses

## Project structure

- `controller` — REST endpoints
- `service` — business logic
- `repository` — database access
- `entity` — database tables
- `dto` — request and response models
- `exception` — custom error handling

## Technology stack

- Java 17
- Spring Boot 3.3.x
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## Run the project

1. Create a MySQL database called `employee_management`.
2. Update the database username and password in `src/main/resources/application.properties`.
3. Run:

```bash
mvn spring-boot:run
```

## Database setup

```sql
CREATE DATABASE employee_management;
```

## Sample data

The app seeds sample data automatically when the database is empty.

## API examples

### Create employer

```http
POST /api/employers
Content-Type: application/json

{
  "name": "ABC Technologies",
  "arabicName": "إيه بي سي للتقنية",
  "registrationMonth": "October",
  "debitCardNumber": "1234567890123456"
}
```

### Register employee

```http
POST /api/employers/1/employees
Content-Type: application/json

{
  "name": "Rahul Kumar",
  "creditAccountNumber": "9876543210",
  "monthSalaryAmount": 50000
}
```

### Pay salary

```http
POST /api/transactions/pay
Content-Type: application/json

{
  "employerId": 1,
  "employeeId": 1
}
```

### Get transactions by date

```http
GET /api/transactions?date=2026-10-06
```

## Beginner guide

The flow is straightforward:

- Database stores the tables
- Entity classes map to the database tables
- Repository classes read and write data
- Service classes contain the business rules
- Controller classes expose REST routes

The salary flow looks like this:

```text
Employer -> Select employee
  -> Check employer exists
  -> Check employee exists
  -> Check employee belongs to employer
  -> Check employee is active
  -> Check current month salary already paid
  -> Create PENDING transaction
  -> Simulate processing
  -> Save SUCCESS or FAILED
```

## Postman testing sequence

1. Create an employer
2. Register an employee
3. Get all employees of the employer
4. Search for an employee by name
5. Pay an employee
6. Verify the transaction status
7. Get transaction history
8. Deregister the employee
9. Try to pay the deregistered employee and expect failure
10. Try paying the same employee twice in the same month and expect rejection

## Error examples

- Employer not found -> 404
- Employee not found -> 404
- Employee belongs to another employer -> 400
- Employee inactive -> 400
- Duplicate monthly salary -> 400

## Notes

- The API masks the debit card number in responses.
- The project intentionally keeps the design simple and beginner-friendly.
