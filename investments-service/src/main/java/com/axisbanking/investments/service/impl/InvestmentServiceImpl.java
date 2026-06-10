package com.axisbanking.investments.service.impl;

import com.axisbanking.investments.dto.InvestmentDto;
import com.axisbanking.investments.exception.ResourceNotFoundException;
import com.axisbanking.investments.model.*;
import com.axisbanking.investments.repository.InvestmentRepository;
import com.axisbanking.investments.service.InvestmentService;
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
public class InvestmentServiceImpl implements InvestmentService {

    private final InvestmentRepository investmentRepository;

    @Override
    public InvestmentDto createInvestment(InvestmentDto dto) {
        InvestmentType type = InvestmentType.valueOf(dto.getInvestmentType());
        BigDecimal navPrice = dto.getNavPrice() != null ? dto.getNavPrice() : new BigDecimal("100.00");
        BigDecimal units = dto.getInvestedAmount().divide(navPrice, 4, RoundingMode.HALF_UP);

        Investment investment = Investment.builder()
                .investmentNumber(generateInvestmentNumber())
                .customerId(dto.getCustomerId())
                .investmentType(type)
                .schemeName(dto.getSchemeName())
                .investedAmount(dto.getInvestedAmount())
                .currentValue(dto.getInvestedAmount())
                .units(units)
                .navPrice(navPrice)
                .sipAmount(type == InvestmentType.SIP ? dto.getSipAmount() : null)
                .investmentDate(LocalDate.now())
                .status(InvestmentStatus.ACTIVE)
                .build();

        return mapToDto(investmentRepository.save(investment));
    }

    @Override
    public InvestmentDto getInvestmentById(Long id) {
        return mapToDto(investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", id.toString())));
    }

    @Override
    public List<InvestmentDto> getInvestmentsByCustomerId(Long customerId) {
        return investmentRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<InvestmentDto> getAllInvestments() {
        return investmentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public InvestmentDto redeemInvestment(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", id.toString()));
        inv.setStatus(InvestmentStatus.REDEEMED);
        return mapToDto(investmentRepository.save(inv));
    }

    @Override
    public void deleteInvestment(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", id.toString()));
        investmentRepository.delete(inv);
    }

    private String generateInvestmentNumber() {
        return "INV" + (9000000000L + new Random().nextLong(1000000000L));
    }

    private InvestmentDto mapToDto(Investment i) {
        return InvestmentDto.builder()
                .id(i.getId()).investmentNumber(i.getInvestmentNumber())
                .customerId(i.getCustomerId()).investmentType(i.getInvestmentType().name())
                .schemeName(i.getSchemeName()).investedAmount(i.getInvestedAmount())
                .currentValue(i.getCurrentValue()).units(i.getUnits())
                .navPrice(i.getNavPrice()).sipAmount(i.getSipAmount())
                .investmentDate(i.getInvestmentDate()).status(i.getStatus().name())
                .build();
    }
}
