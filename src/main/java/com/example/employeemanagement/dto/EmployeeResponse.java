package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String creditAccountNumber;
    private BigDecimal monthSalaryAmount;
    private boolean isActive;
    private Long employerId;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}
