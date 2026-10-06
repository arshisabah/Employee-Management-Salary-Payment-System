package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.SalaryPaymentRequest;
import com.example.employeemanagement.dto.TransactionResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Employer;
import com.example.employeemanagement.entity.Transaction;
import com.example.employeemanagement.entity.TransactionStatus;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.EmployerRepository;
import com.example.employeemanagement.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(EmployerRepository employerRepository,
                             EmployeeRepository employeeRepository,
                             TransactionRepository transactionRepository) {
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse processSalaryPayment(SalaryPaymentRequest request) {
        Employer employer = employerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (!employee.getEmployer().getId().equals(employer.getId())) {
            throw new IllegalArgumentException("Employee does not belong to this employer");
        }

        if (!employee.isActive()) {
            throw new IllegalArgumentException("Employee is not active");
        }

        if (hasMonthlyPaymentForCurrentMonth(employer.getId(), employee.getId())) {
            throw new IllegalArgumentException("Salary has already been requested for this month");
        }

        Transaction transaction = new Transaction();
        transaction.setEmployer(employer);
        transaction.setEmployee(employee);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setAmount(employee.getMonthSalaryAmount());

        Transaction savedTransaction = transactionRepository.save(transaction);

        if (employee.getId() % 2 == 0L) {
            savedTransaction.setStatus(TransactionStatus.FAILED);
        } else {
            savedTransaction.setStatus(TransactionStatus.SUCCESS);
        }

        Transaction updatedTransaction = transactionRepository.save(savedTransaction);
        return mapToResponse(updatedTransaction);
    }

    public List<TransactionResponse> getAllTransactions(String date, String from, String to) {
        List<Transaction> transactions;

        boolean hasDate = hasText(date);
        boolean hasFrom = hasText(from);
        boolean hasTo = hasText(to);

        if (hasDate) {
            LocalDate localDate = parseDate(date, "date");
            transactions = transactionRepository.findCreatedBetween(
                    localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
        } else if (hasFrom || hasTo) {
            if (!(hasFrom && hasTo)) {
                throw new IllegalArgumentException("Both 'from' and 'to' dates are required for a date range");
            }
            LocalDate startDate = parseDate(from, "from");
            LocalDate endDate = parseDate(to, "to");
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("'from' date must not be after 'to' date");
            }
            transactions = transactionRepository.findCreatedBetween(
                    startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        } else {
            transactions = transactionRepository.findAllByOrderByCreateTimestampDesc();
        }

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByEmployer(Long employerId) {
        employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        return transactionRepository.findByEmployerIdOrderByCreateTimestampDesc(employerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByEmployee(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return transactionRepository.findByEmployeeIdOrderByCreateTimestampDesc(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private boolean hasMonthlyPaymentForCurrentMonth(Long employerId, Long employeeId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime start = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endExclusive = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<Transaction> existingTransactions = transactionRepository.findPaymentsInPeriod(
                employerId,
                employeeId,
                Set.of(TransactionStatus.PENDING, TransactionStatus.SUCCESS),
                start,
                endExclusive
        );

        return !existingTransactions.isEmpty();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private LocalDate parseDate(String value, String paramName) {
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid '" + paramName + "' date. Use format yyyy-MM-dd");
        }
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setEmployerId(transaction.getEmployer().getId());
        response.setEmployeeId(transaction.getEmployee().getId());
        response.setStatus(transaction.getStatus().name());
        response.setAmount(transaction.getAmount());
        response.setCreateTimestamp(transaction.getCreateTimestamp());
        response.setUpdateTimestamp(transaction.getUpdateTimestamp());
        return response;
    }
}
