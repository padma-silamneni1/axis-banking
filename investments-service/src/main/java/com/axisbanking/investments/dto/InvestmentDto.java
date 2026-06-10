package com.axisbanking.investments.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvestmentDto {

    private Long id;
    private String investmentNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Investment type is required")
    private String investmentType;

    @NotBlank(message = "Scheme name is required")
    private String schemeName;

    @NotNull(message = "Invested amount is required")
    @DecimalMin(value = "500.00", message = "Minimum investment amount is 500")
    private BigDecimal investedAmount;

    private BigDecimal currentValue;
    private BigDecimal units;
    private BigDecimal navPrice;
    private BigDecimal sipAmount;
    private LocalDate investmentDate;
    private String status;
}
