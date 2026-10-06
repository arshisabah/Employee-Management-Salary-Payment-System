package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmployerId(Long employerId);

    List<Employee> findByEmployerIdAndNameContainingIgnoreCase(Long employerId, String name);

    Optional<Employee> findByIdAndEmployerId(Long id, Long employerId);
}
