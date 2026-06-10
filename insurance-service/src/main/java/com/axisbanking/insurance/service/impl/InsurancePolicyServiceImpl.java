package com.axisbanking.insurance.service.impl;

import com.axisbanking.insurance.dto.InsurancePolicyDto;
import com.axisbanking.insurance.exception.ResourceNotFoundException;
import com.axisbanking.insurance.model.*;
import com.axisbanking.insurance.repository.InsurancePolicyRepository;
import com.axisbanking.insurance.service.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;

    @Override
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

        return mapToDto(policyRepository.save(policy));
    }

    @Override
    public InsurancePolicyDto getPolicyById(Long id) {
        return mapToDto(policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InsurancePolicy", "id", id.toString())));
    }

    @Override
    public List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<InsurancePolicyDto> getAllPolicies() {
        return policyRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public InsurancePolicyDto cancelPolicy(Long id) {
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InsurancePolicy", "id", id.toString()));
        policy.setStatus(PolicyStatus.CANCELLED);
        return mapToDto(policyRepository.save(policy));
    }

    @Override
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
