# Employee Management & Salary Payment System

Act as an expert **Java Spring Boot backend developer** and build a beginner-friendly **Employee Management and Salary Payment System**.

The goal is to create a simple backend application where an **Employer** can manage their employees and make a **one-time monthly salary payment** to an employee.

The project should be easy for a beginner to understand, run, test, and extend.

---

## 1. Technology Stack

Use only the following technologies:

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Lombok (optional)
- Bean Validation if required

Do **NOT** use:

- JWT
- Spring Security
- Microservices
- Kafka
- Redis
- Docker
- OAuth
- Complex design patterns
- Complex event-driven architecture
- External payment gateways
- Cloud services

Keep everything inside a single Spring Boot application.

---

# 2. Main Actors

There are two logical actors:

### Employer

An employer can:

1. Register an employee
2. Deregister an employee
3. View all employees under them
4. Search employees by name
5. Make a one-time monthly salary payment to an employee
6. View salary/payment transactions
7. View transactions sorted by transaction date

### Employee

The employee is mainly represented as a record in the system.

The employee does not need login functionality.

---

# 3. Database Design

Create exactly **3 main tables**.

## Table 1: employer

Columns:

```text
employer
--------------------------------
id
name
registration_month
create_timestamp
update_timestamp
debit_card_number
```

Suggested types:

```text
id                  BIGINT PRIMARY KEY AUTO_INCREMENT
name                VARCHAR(100)
registration_month  VARCHAR(20)
create_timestamp    DATETIME
update_timestamp    DATETIME
debit_card_number   VARCHAR(50)
```

---

## Table 2: employee

Columns:

```text
employee
--------------------------------
id
name
credit_account_number
month_salary_amount
is_active
employer_id
create_timestamp
update_timestamp
```

Suggested types:

```text
id                    BIGINT PRIMARY KEY AUTO_INCREMENT
name                  VARCHAR(100)
credit_account_number VARCHAR(50)
month_salary_amount   DECIMAL(12,2)
is_active              BOOLEAN
employer_id            BIGINT
create_timestamp       DATETIME
update_timestamp       DATETIME
```

Relationship:

```text
employee.employer_id
        ↓
employer.id
```

One employer can have many employees.

```text
Employer 1 -------- * Employee
```

---

## Table 3: transaction

Columns:

```text
transaction
--------------------------------
id
employer_id
employee_id
status
amount
create_timestamp
update_timestamp
```

Suggested types:

```text
id                BIGINT PRIMARY KEY AUTO_INCREMENT
employer_id       BIGINT
employee_id       BIGINT
status            VARCHAR(20)
amount            DECIMAL(12,2)
create_timestamp  DATETIME
update_timestamp  DATETIME
```

Relationships:

```text
transaction.employer_id
        ↓
employer.id

transaction.employee_id
        ↓
employee.id
```

---

# 4. Transaction Status

The salary transaction should have only these statuses:

```text
PENDING
SUCCESS
FAILED
```

The normal flow should be:

```text
Employer submits salary request
             ↓
          PENDING
             ↓
      Payment processing
        ↙          ↘
    SUCCESS       FAILED
```

For this beginner-friendly project, do not integrate an actual bank/payment gateway.

You can simulate the payment result.

For example:

```text
POST /api/transactions/pay
```

Initially create the transaction with:

```text
status = PENDING
```

Then update it to either:

```text
SUCCESS
```

or

```text
FAILED
```

You can keep the simulation simple.

---

# 5. Important Business Rules

Implement the following rules.

### Employee registration

An employer can register an employee.

When an employee is registered:

```text
is_active = true
```

The employee must belong to an existing employer.

---

### Employee deregistration

Deregistering an employee should **not delete the employee record**.

Instead:

```text
is_active = false
```

This preserves historical salary transactions.

---

### Salary payment

An employer can pay an employee only when:

```text
employee.is_active == true
```

The employee must belong to the employer making the payment.

The salary amount should come from:

```text
employee.month_salary_amount
```

The employer should not arbitrarily change the salary amount during payment.

---

### One-time monthly payment

An employee should receive salary only once per month.

Before creating a new salary transaction, check whether a successful or pending transaction already exists for:

```text
same employer
+
same employee
+
same month
```

If a payment already exists for that month, reject the new payment request.

Example:

```text
Employee: Rahul
Salary: ₹50,000
Month: October 2026

First request → allowed

Second request in October → reject
```

This prevents duplicate monthly salary payments.

---

# 6. REST API Design

Keep the endpoints simple and beginner-friendly.

## Employer APIs

### Create Employer

```http
POST /api/employers
```

Request:

```json
{
  "name": "ABC Technologies",
  "registrationMonth": "October",
  "debitCardNumber": "1234567890123456"
}
```

Response:

```json
{
  "id": 1,
  "name": "ABC Technologies",
  "registrationMonth": "October",
  "debitCardNumber": "1234567890123456"
}
```

---

### Get Employer

```http
GET /api/employers/{employerId}
```

---

### Get Employees Under Employer

```http
GET /api/employers/{employerId}/employees
```

Return all employees belonging to the employer.

---

### Search Employee by Name

```http
GET /api/employers/{employerId}/employees/search?name=rahul
```

The search should be case-insensitive.

Example:

```text
rahul
Rahul
RAHUL
```

should all be able to find:

```text
Rahul Kumar
```

---

# 7. Employee APIs

### Register Employee

```http
POST /api/employers/{employerId}/employees
```

Request:

```json
{
  "name": "Rahul Kumar",
  "creditAccountNumber": "9876543210",
  "monthSalaryAmount": 50000
}
```

The system should automatically set:

```text
isActive = true
```

---

### Deregister Employee

```http
PUT /api/employees/{employeeId}/deregister
```

Change:

```text
is_active = false
```

Do not physically delete the employee.

---

### Get Employee

```http
GET /api/employees/{employeeId}
```

---

# 8. Salary Payment API

Create a simple endpoint:

```http
POST /api/transactions/pay
```

Request:

```json
{
  "employerId": 1,
  "employeeId": 10
}
```

The backend should:

### Step 1

Check whether employer exists.

### Step 2

Check whether employee exists.

### Step 3

Check that employee belongs to the employer.

### Step 4

Check:

```text
employee.is_active == true
```

### Step 5

Get salary from:

```text
employee.month_salary_amount
```

### Step 6

Check whether salary has already been requested for the current month.

### Step 7

Create transaction:

```text
status = PENDING
amount = employee.month_salary_amount
```

### Step 8

Simulate payment processing.

The final transaction should become:

```text
SUCCESS
```

or:

```text
FAILED
```

---

# 9. Transaction APIs

### Get All Transactions

```http
GET /api/transactions
```

Return transactions ordered by:

```text
create_timestamp DESC
```

Newest transactions should appear first.

---

### Get Employer Transactions

```http
GET /api/employers/{employerId}/transactions
```

---

### Get Employee Transactions

```http
GET /api/employees/{employeeId}/transactions
```

---

### Get Transactions by Date

Support simple date filtering:

```http
GET /api/transactions?date=2026-10-06
```

Return transactions created on that date.

---

### Get Transactions by Date Range

If implementing date range filtering, use:

```http
GET /api/transactions?from=2026-10-01&to=2026-10-06
```

Return transactions ordered by transaction creation date.

Do not introduce complicated filtering frameworks.

---

# 10. Recommended Project Structure

Use a simple layered architecture:

```text
src/main/java/com/example/employeemanagement/

├── controller/
│   ├── EmployerController.java
│   ├── EmployeeController.java
│   └── TransactionController.java
│
├── service/
│   ├── EmployerService.java
│   ├── EmployeeService.java
│   └── TransactionService.java
│
├── repository/
│   ├── EmployerRepository.java
│   ├── EmployeeRepository.java
│   └── TransactionRepository.java
│
├── entity/
│   ├── Employer.java
│   ├── Employee.java
│   └── Transaction.java
│
├── dto/
│   ├── EmployerRequest.java
│   ├── EmployeeRequest.java
│   ├── SalaryPaymentRequest.java
│   └── TransactionResponse.java
│
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
│
└── EmployeeManagementApplication.java
```

Keep the architecture straightforward:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

---

# 11. Entity Relationships

Use simple JPA relationships.

### Employer

```java
@OneToMany
private List<Employee> employees;
```

### Employee

```java
@ManyToOne
@JoinColumn(name = "employer_id")
private Employer employer;
```

Transaction should reference both:

```java
@ManyToOne
@JoinColumn(name = "employer_id")
private Employer employer;

@ManyToOne
@JoinColumn(name = "employee_id")
private Employee employee;
```

Avoid complicated cascade configurations unless absolutely necessary.

---

# 12. Important Repository Methods

Keep repository methods simple.

For example:

```java
List<Employee> findByEmployerId(Long employerId);
```

For search:

```java
List<Employee> findByEmployerIdAndNameContainingIgnoreCase(
    Long employerId,
    String name
);
```

For checking monthly payment:

Create a simple repository query that checks transactions for:

```text
employer
employee
date range for current month
status PENDING or SUCCESS
```

Do not use complicated native SQL unless necessary.

---

# 13. Error Handling

Implement simple global exception handling.

Examples:

### Employer not found

```http
404 NOT FOUND
```

```json
{
  "message": "Employer not found"
}
```

### Employee not found

```http
404 NOT FOUND
```

```json
{
  "message": "Employee not found"
}
```

### Employee does not belong to employer

```http
400 BAD REQUEST
```

```json
{
  "message": "Employee does not belong to this employer"
}
```

### Employee inactive

```http
400 BAD REQUEST
```

```json
{
  "message": "Employee is not active"
}
```

### Duplicate monthly payment

```http
400 BAD REQUEST
```

```json
{
  "message": "Salary has already been requested for this month"
}
```

---

# 14. Database Configuration

Use MySQL.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true
```

Create the database:

```sql
CREATE DATABASE employee_management;
```

Let Hibernate create/update the three tables.

Do not introduce Flyway or Liquibase for this beginner project.

---

# 15. Timestamp Handling

Use:

```java
LocalDateTime
```

for:

```text
createTimestamp
updateTimestamp
```

Automatically populate them using JPA lifecycle methods or Spring Data auditing.

Keep the implementation simple.

---

# 16. Transaction Date Rendering

The transaction API should return timestamps so the frontend can render salary requests based on transaction date.

Example response:

```json
[
  {
    "id": 10,
    "employerId": 1,
    "employeeId": 5,
    "status": "SUCCESS",
    "amount": 50000,
    "createTimestamp": "2026-10-06T10:30:00",
    "updateTimestamp": "2026-10-06T10:30:05"
  },
  {
    "id": 9,
    "employerId": 1,
    "employeeId": 4,
    "status": "FAILED",
    "amount": 40000,
    "createTimestamp": "2026-10-05T12:20:00",
    "updateTimestamp": "2026-10-05T12:20:03"
  }
]
```

The frontend can then display:

```text
October 6, 2026
    Rahul Kumar
    ₹50,000
    SUCCESS

October 5, 2026
    Priya Sharma
    ₹40,000
    FAILED
```

---

# 17. Complete Salary Flow

Implement this exact flow:

```text
Employer
   |
   | Select Employee
   ↓
Check Employee
   |
   ├── Does not exist → Error
   |
   ├── Belongs to another employer → Error
   |
   ├── Inactive → Error
   |
   ↓
Check Current Month Payment
   |
   ├── Already exists → Error
   |
   ↓
Create Transaction
   |
   | status = PENDING
   ↓
Process Payment
   |
   ├── Success → SUCCESS
   |
   └── Failure → FAILED
```

---

# 18. Example Database Data

Create sample data for testing.

### Employer

```text
1 | ABC Technologies | October | 2026-10-01 | 2026-10-01 | 1234567890123456
```

### Employees

```text
1 | Rahul Kumar | 9876543210 | 50000 | true | 1
2 | Priya Sharma | 9876543211 | 45000 | true | 1
3 | Amit Singh | 9876543212 | 40000 | false | 1
```

### Transactions

```text
1 | 1 | 1 | SUCCESS | 50000
2 | 1 | 2 | PENDING | 45000
3 | 1 | 1 | FAILED | 50000
```

---

# 19. Testing

Use Postman to test all APIs.

Provide a complete testing sequence:

### Step 1

Create employer.

### Step 2

Register employee.

### Step 3

Get employer's employees.

### Step 4

Search employee.

### Step 5

Pay employee.

### Step 6

Verify transaction status.

### Step 7

Get transaction history.

### Step 8

Deregister employee.

### Step 9

Try paying deregistered employee and verify that the request fails.

### Step 10

Try paying the same employee twice in the same month and verify that duplicate payment is rejected.

---

# 20. Code Quality Requirements

Follow beginner-friendly coding practices.

Use:

- Meaningful class names
- Meaningful variable names
- Small methods
- Constructor injection
- Simple DTOs
- Service layer for business logic
- Repository layer for database operations
- Proper HTTP status codes
- Simple exception handling

Avoid:

- Huge service classes
- Complex generics
- Streams everywhere
- Unnecessary interfaces
- Design patterns that are not needed
- Over-engineering
- Complex SQL
- Reactive programming
- Async programming

---

# 21. Important Security Note

This is a learning/demo application.

Do not implement authentication or authorization.

However, do not expose the full debit card number in normal API responses.

For example, internally the database may contain:

```text
1234567890123456
```

but an API response should preferably show:

```text
************3456
```

Keep this implementation simple.

---

# 22. Expected Deliverables

Generate the complete working Spring Boot project.

Provide:

1. `pom.xml`
2. Main Spring Boot class
3. Employer entity
4. Employee entity
5. Transaction entity
6. Employer DTOs
7. Employee DTOs
8. Salary payment DTO
9. Repositories
10. Services
11. Controllers
12. Exception classes
13. Global exception handler
14. `application.properties`
15. MySQL database setup instructions
16. Sample data
17. Complete API documentation
18. Postman testing examples

---

# 23. Explanation Requirement

After generating the code, explain the project like a beginner tutorial.

Explain:

```text
Database
   ↓
Entity
   ↓
Repository
   ↓
Service
   ↓
Controller
   ↓
API
```

Then explain the salary payment flow with a real example.

For example:

```text
Employer: ABC Technologies

Employee:
Rahul Kumar
Salary: ₹50,000

Employer clicks "Pay Salary"

        ↓

POST /api/transactions/pay

        ↓

Check employer

        ↓

Check employee

        ↓

Check employee belongs to employer

        ↓

Check employee is active

        ↓

Check whether October salary
has already been paid

        ↓

Create transaction

Status = PENDING

        ↓

Process payment

        ↓

Status = SUCCESS

        ↓

Transaction appears in history
```

---

# 24. Final Requirement

The most important requirement is:

> **Keep this project beginner-friendly.**

I should be able to understand the complete code as a Java/Spring Boot beginner.

Prefer simple code over sophisticated code.

Do not add functionality that was not requested.

Use exactly these three main entities:

```text
Employer
Employee
Transaction
```

and keep the application as a single simple Spring Boot + MySQL project.