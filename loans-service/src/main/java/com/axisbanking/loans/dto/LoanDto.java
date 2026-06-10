package com.axisbanking.loans.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanDto {

    private Long id;
    private String loanNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "10000.00", message = "Minimum loan amount is 10000")
    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Minimum tenure is 6 months")
    private Integer tenureMonths;

    private BigDecimal emiAmount;
    private BigDecimal outstandingAmount;
    private BigDecimal totalInterest;
    private LocalDate disbursementDate;
    private LocalDate endDate;
    private String status;
}
