package com.axisbanking.insurance.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseDto {
    private String statusCode;
    private String statusMessage;
}
