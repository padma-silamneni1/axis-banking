package com.axisbanking.investments.repository;

import com.axisbanking.investments.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    Optional<Investment> findByInvestmentNumber(String investmentNumber);

    List<Investment> findByCustomerId(Long customerId);
}
