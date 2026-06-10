package com.axisbanking.loans.service.impl;

import com.axisbanking.common.event.EventPublisher;
import com.axisbanking.common.event.EventType;
import com.axisbanking.common.event.KafkaTopics;
import com.axisbanking.loans.dto.LoanDto;
import com.axisbanking.loans.exception.ResourceNotFoundException;
import com.axisbanking.loans.model.*;
import com.axisbanking.loans.repository.LoanRepository;
import com.axisbanking.loans.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final EventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "loansByCustomer", allEntries = true)
    public LoanDto applyForLoan(LoanDto dto) {
        LoanType type = LoanType.valueOf(dto.getLoanType());
        BigDecimal interestRate = getInterestRate(type);
        BigDecimal emi = calculateEMI(dto.getLoanAmount(), interestRate, dto.getTenureMonths());
        BigDecimal totalInterest = emi.multiply(BigDecimal.valueOf(dto.getTenureMonths()))
                .subtract(dto.getLoanAmount());

        Loan loan = Loan.builder()
                .loanNumber(generateLoanNumber())
                .customerId(dto.getCustomerId())
                .loanType(type)
                .loanAmount(dto.getLoanAmount())
                .interestRate(interestRate)
                .tenureMonths(dto.getTenureMonths())
                .emiAmount(emi)
                .outstandingAmount(dto.getLoanAmount())
                .totalInterest(totalInterest)
                .endDate(LocalDate.now().plusMonths(dto.getTenureMonths()))
                .status(LoanStatus.APPLIED)
                .build();

        Loan saved = loanRepository.save(loan);

        Map<String, Object> payload = new HashMap<>();
        payload.put("loanNumber", saved.getLoanNumber());
        payload.put("customerId", saved.getCustomerId());
        payload.put("loanType", type.name());
        payload.put("loanAmount", saved.getLoanAmount());
        payload.put("emiAmount", saved.getEmiAmount());
        eventPublisher.publish(KafkaTopics.LOAN_EVENTS, EventType.LOAN_APPLIED,
                saved.getId().toString(), "Loan", "APPLY", payload);

        log.info("Loan applied: {} type: {} amount: {}", saved.getLoanNumber(), type, saved.getLoanAmount());
        return mapToDto(saved);
    }

    @Override
    @Cacheable(value = "loans", key = "#id")
    public LoanDto getLoanById(Long id) {
        return mapToDto(loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id.toString())));
    }

    @Override
    @Cacheable(value = "loans", key = "'num:' + #loanNumber")
    public LoanDto getLoanByNumber(String loanNumber) {
        return mapToDto(loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanNumber)));
    }

    @Override
    @Cacheable(value = "loansByCustomer", key = "#customerId")
    public List<LoanDto> getLoansByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getAllLoans() {
        return loanRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "loans", key = "#id")
    @CacheEvict(value = "loansByCustomer", allEntries = true)
    public LoanDto approveLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id.toString()));
        loan.setStatus(LoanStatus.APPROVED);
        loan.setDisbursementDate(LocalDate.now());
        Loan saved = loanRepository.save(loan);

        Map<String, Object> payload = new HashMap<>();
        payload.put("loanNumber", saved.getLoanNumber());
        payload.put("status", "APPROVED");
        eventPublisher.publish(KafkaTopics.LOAN_EVENTS, EventType.LOAN_APPROVED,
                id.toString(), "Loan", "APPROVE", payload);

        return mapToDto(saved);
    }

    @Override
    @CachePut(value = "loans", key = "#id")
    @CacheEvict(value = "loansByCustomer", allEntries = true)
    public LoanDto closeLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id.toString()));
        loan.setStatus(LoanStatus.CLOSED);
        loan.setOutstandingAmount(BigDecimal.ZERO);
        Loan saved = loanRepository.save(loan);

        Map<String, Object> payload = new HashMap<>();
        payload.put("loanNumber", saved.getLoanNumber());
        payload.put("status", "CLOSED");
        eventPublisher.publish(KafkaTopics.LOAN_EVENTS, EventType.LOAN_CLOSED,
                id.toString(), "Loan", "CLOSE", payload);

        return mapToDto(saved);
    }

    @Override
    @CacheEvict(value = {"loans", "loansByCustomer"}, allEntries = true)
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id.toString()));
        loanRepository.delete(loan);
    }

    private BigDecimal getInterestRate(LoanType type) {
        return switch (type) {
            case HOME_LOAN -> new BigDecimal("8.50");
            case PERSONAL_LOAN -> new BigDecimal("12.00");
            case AUTO_LOAN -> new BigDecimal("9.25");
            case EDUCATION_LOAN -> new BigDecimal("7.50");
            case GOLD_LOAN -> new BigDecimal("8.00");
            case BUSINESS_LOAN -> new BigDecimal("14.00");
        };
    }

    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int months) {
        double r = annualRate.doubleValue() / 12 / 100;
        double p = principal.doubleValue();
        double emi = (p * r * Math.pow(1 + r, months)) / (Math.pow(1 + r, months) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    private String generateLoanNumber() {
        return "LN" + (9000000000L + new Random().nextLong(1000000000L));
    }

    private LoanDto mapToDto(Loan l) {
        return LoanDto.builder()
                .id(l.getId()).loanNumber(l.getLoanNumber())
                .customerId(l.getCustomerId()).loanType(l.getLoanType().name())
                .loanAmount(l.getLoanAmount()).interestRate(l.getInterestRate())
                .tenureMonths(l.getTenureMonths()).emiAmount(l.getEmiAmount())
                .outstandingAmount(l.getOutstandingAmount()).totalInterest(l.getTotalInterest())
                .disbursementDate(l.getDisbursementDate()).endDate(l.getEndDate())
                .status(l.getStatus().name()).build();
    }
}
