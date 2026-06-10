package com.axisbanking.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15)
    private String mobileNumber;

    @Size(max = 10)
    private String panNumber;

    @Size(max = 12)
    private String aadhaarNumber;

    private String address;

    private String dateOfBirth;
}
