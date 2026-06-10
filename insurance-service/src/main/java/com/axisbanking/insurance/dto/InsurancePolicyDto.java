package com.axisbanking.insurance.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InsurancePolicyDto {

    private Long id;
    private String policyNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Insurance type is required")
    private String insuranceType;

    @NotBlank(message = "Policy name is required")
    private String policyName;

    @NotNull(message = "Sum assured is required")
    @DecimalMin(value = "50000.00", message = "Minimum sum assured is 50000")
    private BigDecimal sumAssured;

    @NotNull(message = "Premium amount is required")
    private BigDecimal premiumAmount;

    private String premiumFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String nomineeName;
    private String nomineeRelation;
    private String status;
}
