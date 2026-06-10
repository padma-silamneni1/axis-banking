package com.axisbanking.payments.service.impl;

import com.axisbanking.payments.dto.PaymentDto;
import com.axisbanking.payments.exception.ResourceNotFoundException;
import com.axisbanking.payments.model.*;
import com.axisbanking.payments.repository.PaymentRepository;
import com.axisbanking.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto initiatePayment(PaymentDto dto) {
        PaymentType type = PaymentType.valueOf(dto.getPaymentType());

        Payment payment = Payment.builder()
                .transactionId(generateTransactionId(type))
                .customerId(dto.getCustomerId())
                .fromAccount(dto.getFromAccount())
                .toAccount(dto.getToAccount())
                .beneficiaryName(dto.getBeneficiaryName())
                .beneficiaryIfsc(dto.getBeneficiaryIfsc())
                .paymentType(type)
                .amount(dto.getAmount())
                .remarks(dto.getRemarks())
                .upiId(dto.getUpiId())
                .status(PaymentStatus.COMPLETED)
                .build();

        return mapToDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        return mapToDto(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id.toString())));
    }

    @Override
    public PaymentDto getPaymentByTransactionId(String transactionId) {
        return mapToDto(paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId)));
    }

    @Override
    public List<PaymentDto> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private String generateTransactionId(PaymentType type) {
        String prefix = switch (type) {
            case UPI -> "UPI";
            case NEFT -> "NEFT";
            case RTGS -> "RTGS";
            case IMPS -> "IMPS";
            case BILL_PAYMENT -> "BILL";
            case FUND_TRANSFER -> "FT";
        };
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private PaymentDto mapToDto(Payment p) {
        return PaymentDto.builder()
                .id(p.getId()).transactionId(p.getTransactionId())
                .customerId(p.getCustomerId()).fromAccount(p.getFromAccount())
                .toAccount(p.getToAccount()).beneficiaryName(p.getBeneficiaryName())
                .beneficiaryIfsc(p.getBeneficiaryIfsc()).paymentType(p.getPaymentType().name())
                .amount(p.getAmount()).remarks(p.getRemarks())
                .upiId(p.getUpiId()).status(p.getStatus().name())
                .transactionDate(p.getTransactionDate()).build();
    }
}
