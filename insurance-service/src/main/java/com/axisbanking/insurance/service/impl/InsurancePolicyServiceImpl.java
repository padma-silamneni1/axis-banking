package com.axisbanking.insurance.service.impl;

import com.axisbanking.common.event.EventPublisher;
import com.axisbanking.common.event.EventType;
import com.axisbanking.common.event.KafkaTopics;
import com.axisbanking.insurance.dto.InsurancePolicyDto;
import com.axisbanking.insurance.exception.ResourceNotFoundException;
import com.axisbanking.insurance.model.*;
import com.axisbanking.insurance.repository.InsurancePolicyRepository;
import com.axisbanking.insurance.service.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    private final EventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "policiesByCustomer", allEntries = true)
    public InsurancePolicyDto createPolicy(InsurancePolicyDto dto) {
        InsuranceType type = InsuranceType.valueOf(dto.getInsuranceType());

        InsurancePolicy policy = InsurancePolicy.builder()
                .policyNumber(generatePolicyNumber())
                .customerId(dto.getCustomerId())
                .insuranceType(type)
                .policyName(dto.getPolicyName())
                .sumAssured(dto.getSumAssured())
                .premiumAmount(dto.getPremiumAmount())
                .premiumFrequency(dto.getPremiumFrequency() != null ? dto.getPremiumFrequency() : "YEARLY")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(type == InsuranceType.LIFE_INSURANCE ? 20 : 1))
                .nomineeName(dto.getNomineeName())
                .nomineeRelation(dto.getNomineeRelation())
                .status(PolicyStatus.ACTIVE)
                .build();

        InsurancePolicy saved = policyRepository.save(policy);

        Map<String, Object> payload = new HashMap<>();
        payload.put("policyNumber", saved.getPolicyNumber());
        payload.put("customerId", saved.getCustomerId());
        payload.put("insuranceType", type.name());
        payload.put("sumAssured", saved.getSumAssured());
        eventPublisher.publish(KafkaTopics.INSURANCE_EVENTS, EventType.POLICY_CREATED,
                saved.getId().toString(), "InsurancePolicy", "CREATE", payload);

        log.info("Policy created: {} type: {}", saved.getPolicyNumber(), type);
        return mapToDto(saved);
    }

    @Override
    @Cacheable(value = "policies", key = "#id")
    public InsurancePolicyDto getPolicyById(Long id) {
        return mapToDto(policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InsurancePolicy", "id", id.toString())));
    }

    @Override
    @Cacheable(value = "policiesByCustomer", key = "#customerId")
    public List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<InsurancePolicyDto> getAllPolicies() {
        return policyRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"policies", "policiesByCustomer"}, allEntries = true)
    public InsurancePolicyDto cancelPolicy(Long id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InsurancePolicy", "id", id.toString()));
        policy.setStatus(PolicyStatus.CANCELLED);
        InsurancePolicy saved = policyRepository.save(policy);

        Map<String, Object> payload = new HashMap<>();
        payload.put("policyNumber", saved.getPolicyNumber());
        payload.put("status", "CANCELLED");
        eventPublisher.publish(KafkaTopics.INSURANCE_EVENTS, EventType.POLICY_CANCELLED,
                id.toString(), "InsurancePolicy", "CANCEL", payload);

        return mapToDto(saved);
    }

    @Override
    @CacheEvict(value = {"policies", "policiesByCustomer"}, allEntries = true)
    public void deletePolicy(Long id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InsurancePolicy", "id", id.toString()));
        policyRepository.delete(policy);
    }

    private String generatePolicyNumber() {
        return "POL" + (9000000000L + new Random().nextLong(1000000000L));
    }

    private InsurancePolicyDto mapToDto(InsurancePolicy p) {
        return InsurancePolicyDto.builder()
                .id(p.getId()).policyNumber(p.getPolicyNumber())
                .customerId(p.getCustomerId()).insuranceType(p.getInsuranceType().name())
                .policyName(p.getPolicyName()).sumAssured(p.getSumAssured())
                .premiumAmount(p.getPremiumAmount()).premiumFrequency(p.getPremiumFrequency())
                .startDate(p.getStartDate()).endDate(p.getEndDate())
                .nomineeName(p.getNomineeName()).nomineeRelation(p.getNomineeRelation())
                .status(p.getStatus().name()).build();
    }
}
