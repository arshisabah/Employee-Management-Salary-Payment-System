package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Employer;
import com.example.employeemanagement.entity.Transaction;
import com.example.employeemanagement.entity.TransactionStatus;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.EmployerRepository;
import com.example.employeemanagement.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;
    private final TransactionRepository transactionRepository;

    public DataInitializer(EmployerRepository employerRepository,
                          EmployeeRepository employeeRepository,
                          TransactionRepository transactionRepository) {
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (employerRepository.count() > 0) {
            return;
        }

        Employer employer = new Employer();
        employer.setName("ABC Technologies");
        employer.setArabicName("إيه بي سي للتقنية");
        employer.setRegistrationMonth("October");
        employer.setDebitCardNumber("1234567890123456");
        Employer savedEmployer = employerRepository.save(employer);

        Employee employee1 = new Employee();
        employee1.setName("Rahul Kumar");
        employee1.setCreditAccountNumber("9876543210");
        employee1.setMonthSalaryAmount(new BigDecimal("50000.00"));
        employee1.setActive(true);
        employee1.setEmployer(savedEmployer);
        Employee savedEmployee1 = employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Priya Sharma");
        employee2.setCreditAccountNumber("9876543211");
        employee2.setMonthSalaryAmount(new BigDecimal("45000.00"));
        employee2.setActive(true);
        employee2.setEmployer(savedEmployer);
        Employee savedEmployee2 = employeeRepository.save(employee2);

        Employee employee3 = new Employee();
        employee3.setName("Amit Singh");
        employee3.setCreditAccountNumber("9876543212");
        employee3.setMonthSalaryAmount(new BigDecimal("40000.00"));
        employee3.setActive(false);
        employee3.setEmployer(savedEmployer);
        Employee savedEmployee3 = employeeRepository.save(employee3);

        Transaction transaction1 = new Transaction();
        transaction1.setEmployer(savedEmployer);
        transaction1.setEmployee(savedEmployee1);
        transaction1.setStatus(TransactionStatus.SUCCESS);
        transaction1.setAmount(new BigDecimal("50000.00"));
        transaction1.setCreateTimestamp(LocalDateTime.of(2026, 10, 6, 10, 30, 0));
        transaction1.setUpdateTimestamp(LocalDateTime.of(2026, 10, 6, 10, 30, 5));

        Transaction transaction2 = new Transaction();
        transaction2.setEmployer(savedEmployer);
        transaction2.setEmployee(savedEmployee2);
        transaction2.setStatus(TransactionStatus.PENDING);
        transaction2.setAmount(new BigDecimal("45000.00"));
        transaction2.setCreateTimestamp(LocalDateTime.of(2026, 10, 5, 12, 20, 0));
        transaction2.setUpdateTimestamp(LocalDateTime.of(2026, 10, 5, 12, 20, 3));

        Transaction transaction3 = new Transaction();
        transaction3.setEmployer(savedEmployer);
        transaction3.setEmployee(savedEmployee1);
        transaction3.setStatus(TransactionStatus.FAILED);
        transaction3.setAmount(new BigDecimal("50000.00"));
        transaction3.setCreateTimestamp(LocalDateTime.of(2026, 10, 4, 8, 0, 0));
        transaction3.setUpdateTimestamp(LocalDateTime.of(2026, 10, 4, 8, 0, 1));

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);
    }
}
