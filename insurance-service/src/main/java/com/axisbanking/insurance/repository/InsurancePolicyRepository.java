package com.axisbanking.insurance.repository;

import com.axisbanking.insurance.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);

    List<InsurancePolicy> findByCustomerId(Long customerId);
}
