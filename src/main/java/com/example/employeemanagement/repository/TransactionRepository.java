package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Transaction;
import com.example.employeemanagement.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByCreateTimestampDesc();

    List<Transaction> findByEmployerIdOrderByCreateTimestampDesc(Long employerId);

    List<Transaction> findByEmployeeIdOrderByCreateTimestampDesc(Long employeeId);

    // start is inclusive, endExclusive is exclusive (avoids 23:59:59.999999999 rounding issues in MySQL)
    @Query("SELECT t FROM Transaction t "
            + "WHERE t.createTimestamp >= :start AND t.createTimestamp < :endExclusive "
            + "ORDER BY t.createTimestamp DESC")
    List<Transaction> findCreatedBetween(@Param("start") LocalDateTime start,
                                         @Param("endExclusive") LocalDateTime endExclusive);

    @Query("SELECT t FROM Transaction t "
            + "WHERE t.employer.id = :employerId AND t.employee.id = :employeeId "
            + "AND t.status IN :statuses "
            + "AND t.createTimestamp >= :start AND t.createTimestamp < :endExclusive")
    List<Transaction> findPaymentsInPeriod(@Param("employerId") Long employerId,
                                           @Param("employeeId") Long employeeId,
                                           @Param("statuses") Collection<TransactionStatus> statuses,
                                           @Param("start") LocalDateTime start,
                                           @Param("endExclusive") LocalDateTime endExclusive);
}
