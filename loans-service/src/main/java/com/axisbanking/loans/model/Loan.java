package com.axisbanking.loans.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq")
    @SequenceGenerator(name = "loan_seq", sequenceName = "LOAN_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "loan_number", nullable = false, unique = true, length = 20)
    private String loanNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false, length = 30)
    private LoanType loanType;

    @Column(name = "loan_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "emi_amount", precision = 15, scale = 2)
    private BigDecimal emiAmount;

    @Column(name = "outstanding_amount", precision = 15, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "total_interest", precision = 15, scale = 2)
    private BigDecimal totalInterest;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
