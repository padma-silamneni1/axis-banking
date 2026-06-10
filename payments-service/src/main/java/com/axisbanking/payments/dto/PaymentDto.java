package com.axisbanking.payments.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentDto {

    private Long id;
    private String transactionId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "From account is required")
    private String fromAccount;

    @NotBlank(message = "To account is required")
    private String toAccount;

    private String beneficiaryName;
    private String beneficiaryIfsc;

    @NotBlank(message = "Payment type is required")
    private String paymentType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum transfer amount is 1")
    private BigDecimal amount;

    private String remarks;
    private String upiId;
    private String status;
    private LocalDateTime transactionDate;
}
