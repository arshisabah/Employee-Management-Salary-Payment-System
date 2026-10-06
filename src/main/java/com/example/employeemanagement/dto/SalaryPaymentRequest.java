package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryPaymentRequest {

    @NotNull(message = "Employer ID is required")
    private Long employerId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;
}
