package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.SalaryPaymentRequest;
import com.example.employeemanagement.dto.TransactionResponse;
import com.example.employeemanagement.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions/pay")
    public ResponseEntity<TransactionResponse> paySalary(@Valid @RequestBody SalaryPaymentRequest request) {
        TransactionResponse response = transactionService.processSalaryPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return ResponseEntity.ok(transactionService.getAllTransactions(date, from, to));
    }

    @GetMapping("/employers/{employerId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getEmployerTransactions(@PathVariable Long employerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByEmployer(employerId));
    }

    @GetMapping("/employees/{employeeId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getEmployeeTransactions(@PathVariable Long employeeId) {
        return ResponseEntity.ok(transactionService.getTransactionsByEmployee(employeeId));
    }
}
