package com.axisbanking.insurance.service;

import com.axisbanking.insurance.dto.InsurancePolicyDto;
import java.util.List;

public interface InsurancePolicyService {

    InsurancePolicyDto createPolicy(InsurancePolicyDto dto);

    InsurancePolicyDto getPolicyById(Long id);

    List<InsurancePolicyDto> getPoliciesByCustomerId(Long customerId);

    List<InsurancePolicyDto> getAllPolicies();

    InsurancePolicyDto cancelPolicy(Long id);

    void deletePolicy(Long id);
}
