package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Employer;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.EmployerRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployerRepository employerRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EmployerRepository employerRepository) {
        this.employeeRepository = employeeRepository;
        this.employerRepository = employerRepository;
    }

    public EmployeeResponse registerEmployee(Long employerId, EmployeeRequest request) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setCreditAccountNumber(request.getCreditAccountNumber());
        employee.setMonthSalaryAmount(request.getMonthSalaryAmount());
        employee.setActive(true);
        employee.setEmployer(employer);

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    public EmployeeResponse getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return mapToResponse(employee);
    }

    public EmployeeResponse deregisterEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setActive(false);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
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
}
