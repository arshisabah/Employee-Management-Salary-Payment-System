package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {

    @NotBlank(message = "Employer name is required")
    @Size(max = 100, message = "Employer name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Employer Arabic name is required")
    @Size(max = 100, message = "Employer Arabic name must be at most 100 characters")
    private String arabicName;

    @NotBlank(message = "Registration month is required")
    @Size(max = 20, message = "Registration month must be at most 20 characters")
    private String registrationMonth;

    @NotBlank(message = "Debit card number is required")
    @Size(max = 50, message = "Debit card number must be at most 50 characters")
    private String debitCardNumber;
}
