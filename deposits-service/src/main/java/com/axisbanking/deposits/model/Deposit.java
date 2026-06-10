package com.axisbanking.deposits.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposits")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deposit_seq")
    @SequenceGenerator(name = "deposit_seq", sequenceName = "DEPOSIT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "deposit_number", nullable = false, unique = true, length = 20)
    private String depositNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_type", nullable = false, length = 30)
    private DepositType depositType;

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "maturity_amount", precision = 15, scale = 2)
    private BigDecimal maturityAmount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "monthly_installment", precision = 15, scale = 2)
    private BigDecimal monthlyInstallment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DepositStatus status;

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
