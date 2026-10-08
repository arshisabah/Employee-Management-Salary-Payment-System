package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployerRequest;
import com.example.employeemanagement.dto.EmployerResponse;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.service.EmployerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping("/employers")
    public ResponseEntity<EmployerResponse> createEmployer(@Valid @RequestBody EmployerRequest request) {
        EmployerResponse response = employerService.createEmployer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/employers/{employerId}")
    public ResponseEntity<List<EmployerResponse>> getEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(List.of(employerService.getEmployer(employerId)));
    }

    @GetMapping("/employers/{employerId}/employees")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(employerService.getEmployeesByEmployer(employerId));
    }

    @GetMapping("/employers/{employerId}/employees/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployeesByName(
            @PathVariable Long employerId,
            @RequestParam String name) {
        return ResponseEntity.ok(employerService.searchEmployeesByName(employerId, name));
    }
}
