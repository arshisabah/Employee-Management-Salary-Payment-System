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
public class TransactionResponse {
    private Long id;
    private Long employerId;
    private Long employeeId;
    private String status;
    private BigDecimal amount;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}
