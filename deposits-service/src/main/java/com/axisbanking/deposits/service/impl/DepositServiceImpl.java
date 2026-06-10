package com.axisbanking.deposits.service.impl;

import com.axisbanking.deposits.dto.DepositDto;
import com.axisbanking.deposits.exception.ResourceNotFoundException;
import com.axisbanking.deposits.model.*;
import com.axisbanking.deposits.repository.DepositRepository;
import com.axisbanking.deposits.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;

    @Override
    public DepositDto createDeposit(DepositDto dto) {
        DepositType type = DepositType.valueOf(dto.getDepositType());

        BigDecimal interestRate = calculateInterestRate(type, dto.getTenureMonths());
        LocalDate startDate = LocalDate.now();
        LocalDate maturityDate = startDate.plusMonths(dto.getTenureMonths());
        BigDecimal maturityAmount = calculateMaturityAmount(dto.getPrincipalAmount(), interestRate, dto.getTenureMonths());

        Deposit deposit = Deposit.builder()
                .depositNumber(generateDepositNumber())
                .customerId(dto.getCustomerId())
                .accountNumber(dto.getAccountNumber())
                .depositType(type)
                .principalAmount(dto.getPrincipalAmount())
                .interestRate(interestRate)
                .tenureMonths(dto.getTenureMonths())
                .maturityAmount(maturityAmount)
                .startDate(startDate)
                .maturityDate(maturityDate)
                .monthlyInstallment(type == DepositType.RECURRING_DEPOSIT ? dto.getPrincipalAmount() : null)
                .status(DepositStatus.ACTIVE)
                .build();

        Deposit saved = depositRepository.save(deposit);
        return mapToDto(saved);
    }

    @Override
    public DepositDto getDepositById(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", "id", id.toString()));
        return mapToDto(deposit);
    }

    @Override
    public DepositDto getDepositByNumber(String depositNumber) {
        Deposit deposit = depositRepository.findByDepositNumber(depositNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", "depositNumber", depositNumber));
        return mapToDto(deposit);
    }

    @Override
    public List<DepositDto> getDepositsByCustomerId(Long customerId) {
        return depositRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<DepositDto> getAllDeposits() {
        return depositRepository.findAll().stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DepositDto closeDeposit(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", "id", id.toString()));
        deposit.setStatus(DepositStatus.PREMATURE_CLOSED);
        return mapToDto(depositRepository.save(deposit));
    }

    @Override
    public void deleteDeposit(Long id) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", "id", id.toString()));
        depositRepository.delete(deposit);
    }

    private BigDecimal calculateInterestRate(DepositType type, int tenureMonths) {
        if (type == DepositType.TAX_SAVING_FD) return new BigDecimal("7.00");
        if (tenureMonths <= 6) return new BigDecimal("5.50");
        if (tenureMonths <= 12) return new BigDecimal("6.50");
        if (tenureMonths <= 24) return new BigDecimal("7.00");
        return new BigDecimal("7.25");
    }

    private BigDecimal calculateMaturityAmount(BigDecimal principal, BigDecimal rate, int months) {
        BigDecimal r = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        double amount = principal.doubleValue() * Math.pow(1 + r.doubleValue() / 4, (4.0 * months / 12));
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private String generateDepositNumber() {
        return "DEP" + (9000000000L + new Random().nextLong(1000000000L));
    }

    private DepositDto mapToDto(Deposit d) {
        return DepositDto.builder()
                .id(d.getId()).depositNumber(d.getDepositNumber())
                .customerId(d.getCustomerId()).accountNumber(d.getAccountNumber())
                .depositType(d.getDepositType().name()).principalAmount(d.getPrincipalAmount())
                .interestRate(d.getInterestRate()).tenureMonths(d.getTenureMonths())
                .maturityAmount(d.getMaturityAmount()).startDate(d.getStartDate())
                .maturityDate(d.getMaturityDate()).monthlyInstallment(d.getMonthlyInstallment())
                .status(d.getStatus().name()).build();
    }
}
