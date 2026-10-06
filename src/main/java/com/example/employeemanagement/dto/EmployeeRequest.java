package com.example.employeemanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Employee name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Credit account number is required")
    @Size(max = 50, message = "Credit account number must be at most 50 characters")
    private String creditAccountNumber;

    @NotNull(message = "Month salary amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Month salary amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Month salary amount must have at most 10 digits and 2 decimal places")
    private BigDecimal monthSalaryAmount;
}
