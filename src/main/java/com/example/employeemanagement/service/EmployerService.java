package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployerRequest;
import com.example.employeemanagement.dto.EmployerResponse;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Employer;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployeeRepository employeeRepository;

    public EmployerService(EmployerRepository employerRepository, EmployeeRepository employeeRepository) {
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
    }

    public EmployerResponse createEmployer(EmployerRequest request) {
        Employer employer = new Employer();
        employer.setName(request.getName());
        employer.setArabicName(request.getArabicName());
        employer.setRegistrationMonth(request.getRegistrationMonth());
        employer.setDebitCardNumber(request.getDebitCardNumber());

        Employer savedEmployer = employerRepository.save(employer);
        return mapToResponse(savedEmployer);
    }

    public EmployerResponse getEmployer(Long employerId) {
        Employer employer = findEmployerById(employerId);
        return mapToResponse(employer);
    }

    public List<EmployeeResponse> getEmployeesByEmployer(Long employerId) {
        findEmployerById(employerId);
        return employeeRepository.findByEmployerId(employerId)
                .stream()
                .map(this::mapEmployeeToResponse)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponse> searchEmployeesByName(Long employerId, String name) {
        findEmployerById(employerId);
        return employeeRepository.findByEmployerIdAndNameContainingIgnoreCase(employerId, name)
                .stream()
                .map(this::mapEmployeeToResponse)
                .collect(Collectors.toList());
    }

    private Employer findEmployerById(Long employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));
    }

    private EmployerResponse mapToResponse(Employer employer) {
        EmployerResponse response = new EmployerResponse();
        response.setId(employer.getId());
        response.setName(employer.getName());
        response.setArabicName(employer.getArabicName());
        response.setRegistrationMonth(employer.getRegistrationMonth());
        response.setDebitCardNumber(maskCardNumber(employer.getDebitCardNumber()));
        return response;
    }

    private EmployeeResponse mapEmployeeToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setCreditAccountNumber(employee.getCreditAccountNumber());
        response.setMonthSalaryAmount(employee.getMonthSalaryAmount());
        response.setActive(employee.isActive());
        response.setEmployerId(employee.getEmployer().getId());
        response.setCreateTimestamp(employee.getCreateTimestamp());
        response.setUpdateTimestamp(employee.getUpdateTimestamp());
        return response;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return null;
        }
        if (cardNumber.length() <= 4) {
            return "*".repeat(cardNumber.length());
        }
        int hiddenLength = cardNumber.length() - 4;
        return "*".repeat(hiddenLength) + cardNumber.substring(hiddenLength);
    }
}
