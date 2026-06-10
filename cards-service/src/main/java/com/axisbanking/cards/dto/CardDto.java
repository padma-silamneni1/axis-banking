package com.axisbanking.cards.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardDto {

    private Long id;
    private String cardNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotBlank(message = "Card type is required")
    private String cardType;

    private String cardNetwork;
    private BigDecimal creditLimit;
    private BigDecimal availableLimit;
    private BigDecimal outstandingAmount;
    private LocalDate expiryDate;
    private String status;
}
