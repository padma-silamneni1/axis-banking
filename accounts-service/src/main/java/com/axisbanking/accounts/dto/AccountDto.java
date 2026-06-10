package com.axisbanking.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountDto {

    private Long id;

    private String accountNumber;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotBlank(message = "Branch code is required")
    private String branchCode;

    private String ifscCode;

    private BigDecimal balance;

    private String status;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String customerName;
}
