package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "credit_account_number", nullable = false, length = 50)
    private String creditAccountNumber;

    @Column(name = "month_salary_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthSalaryAmount;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @OneToMany(mappedBy = "employee")
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTimestamp = now;
        updateTimestamp = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTimestamp = LocalDateTime.now();
    }
}
