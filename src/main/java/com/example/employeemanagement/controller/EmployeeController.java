package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employers/{employerId}/employees")
    public ResponseEntity<EmployeeResponse> registerEmployee(
            @PathVariable Long employerId,
            @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.registerEmployee(employerId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{employeeId}/deregister")
    public ResponseEntity<EmployeeResponse> deregisterEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.deregisterEmployee(employeeId));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }
}
