package com.axisbanking.payments.service.impl;

import com.axisbanking.common.event.EventPublisher;
import com.axisbanking.common.event.EventType;
import com.axisbanking.common.event.KafkaTopics;
import com.axisbanking.payments.dto.PaymentDto;
import com.axisbanking.payments.exception.ResourceNotFoundException;
import com.axisbanking.payments.model.*;
import com.axisbanking.payments.repository.PaymentRepository;
import com.axisbanking.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "paymentsByCustomer", allEntries = true)
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

        Payment saved = paymentRepository.save(payment);

        Map<String, Object> payload = new HashMap<>();
        payload.put("transactionId", saved.getTransactionId());
        payload.put("fromAccount", saved.getFromAccount());
        payload.put("toAccount", saved.getToAccount());
        payload.put("amount", saved.getAmount());
        payload.put("paymentType", type.name());
        payload.put("status", saved.getStatus().name());
        eventPublisher.publish(KafkaTopics.PAYMENT_EVENTS, EventType.PAYMENT_COMPLETED,
                saved.getId().toString(), "Payment", "INITIATE", payload);

        log.info("Payment completed: {} amount: {} type: {}", saved.getTransactionId(), saved.getAmount(), type);
        return mapToDto(saved);
    }

    @Override
    @Cacheable(value = "payments", key = "#id")
    public PaymentDto getPaymentById(Long id) {
        return mapToDto(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id.toString())));
    }

    @Override
    @Cacheable(value = "payments", key = "'txn:' + #transactionId")
    public PaymentDto getPaymentByTransactionId(String transactionId) {
        return mapToDto(paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId)));
    }

    @Override
    @Cacheable(value = "paymentsByCustomer", key = "#customerId")
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
