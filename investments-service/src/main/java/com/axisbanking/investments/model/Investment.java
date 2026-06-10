package com.axisbanking.investments.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "investment_seq")
    @SequenceGenerator(name = "investment_seq", sequenceName = "INVESTMENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "investment_number", nullable = false, unique = true, length = 20)
    private String investmentNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_type", nullable = false, length = 30)
    private InvestmentType investmentType;

    @Column(name = "scheme_name", nullable = false, length = 200)
    private String schemeName;

    @Column(name = "invested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal investedAmount;

    @Column(name = "current_value", precision = 15, scale = 2)
    private BigDecimal currentValue;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal units;

    @Column(name = "nav_price", precision = 10, scale = 4)
    private BigDecimal navPrice;

    @Column(name = "sip_amount", precision = 15, scale = 2)
    private BigDecimal sipAmount;

    @Column(name = "investment_date", nullable = false)
    private LocalDate investmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvestmentStatus status;

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
