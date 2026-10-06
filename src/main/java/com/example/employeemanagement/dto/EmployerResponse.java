package com.example.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponse {
    private Long id;
    private String name;
    private String arabicName;
    private String registrationMonth;
    private String debitCardNumber;
}
