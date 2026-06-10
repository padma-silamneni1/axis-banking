package com.axisbanking.investments.service.impl;

import com.axisbanking.common.event.EventPublisher;
import com.axisbanking.common.event.EventType;
import com.axisbanking.common.event.KafkaTopics;
import com.axisbanking.investments.dto.InvestmentDto;
import com.axisbanking.investments.exception.ResourceNotFoundException;
import com.axisbanking.investments.model.*;
import com.axisbanking.investments.repository.InvestmentRepository;
import com.axisbanking.investments.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
public class InvestmentServiceImpl implements InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final EventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "investmentsByCustomer", allEntries = true)
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

        Investment saved = investmentRepository.save(investment);

        Map<String, Object> payload = new HashMap<>();
        payload.put("investmentNumber", saved.getInvestmentNumber());
        payload.put("customerId", saved.getCustomerId());
        payload.put("investmentType", type.name());
        payload.put("investedAmount", saved.getInvestedAmount());
        eventPublisher.publish(KafkaTopics.INVESTMENT_EVENTS, EventType.INVESTMENT_CREATED,
                saved.getId().toString(), "Investment", "CREATE", payload);

        log.info("Investment created: {} type: {} amount: {}", saved.getInvestmentNumber(), type, saved.getInvestedAmount());
        return mapToDto(saved);
    }

    @Override
    @Cacheable(value = "investments", key = "#id")
    public InvestmentDto getInvestmentById(Long id) {
        return mapToDto(investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", id.toString())));
    }

    @Override
    @Cacheable(value = "investmentsByCustomer", key = "#customerId")
    public List<InvestmentDto> getInvestmentsByCustomerId(Long customerId) {
        return investmentRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<InvestmentDto> getAllInvestments() {
        return investmentRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"investments", "investmentsByCustomer"}, allEntries = true)
    public InvestmentDto redeemInvestment(Long id) {
        Investment inv = investmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investment", "id", id.toString()));
        inv.setStatus(InvestmentStatus.REDEEMED);
        Investment saved = investmentRepository.save(inv);

        Map<String, Object> payload = new HashMap<>();
        payload.put("investmentNumber", saved.getInvestmentNumber());
        payload.put("status", "REDEEMED");
        payload.put("currentValue", saved.getCurrentValue());
        eventPublisher.publish(KafkaTopics.INVESTMENT_EVENTS, EventType.INVESTMENT_REDEEMED,
                id.toString(), "Investment", "REDEEM", payload);

        return mapToDto(saved);
    }

    @Override
    @CacheEvict(value = {"investments", "investmentsByCustomer"}, allEntries = true)
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
