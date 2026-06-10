package com.axisbanking.deposits.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DepositDto {

    private Long id;
    private String depositNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Deposit type is required")
    private String depositType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "1000.00", message = "Minimum deposit amount is 1000")
    private BigDecimal principalAmount;

    private BigDecimal interestRate;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Minimum tenure is 1 month")
    private Integer tenureMonths;

    private BigDecimal maturityAmount;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private BigDecimal monthlyInstallment;
    private String status;
}
