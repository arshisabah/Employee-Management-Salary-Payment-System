package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // nullable so existing employer rows survive ddl-auto=update
    @Column(name = "arabic_name", length = 100)
    private String arabicName;

    @Column(name = "registration_month", nullable = false, length = 20)
    private String registrationMonth;

    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "debit_card_number", nullable = false, length = 50)
    private String debitCardNumber;

    @OneToMany(mappedBy = "employer")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "employer")
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
