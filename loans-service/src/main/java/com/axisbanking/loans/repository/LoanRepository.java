package com.axisbanking.loans.repository;

import com.axisbanking.loans.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByLoanNumber(String loanNumber);

    List<Loan> findByCustomerId(Long customerId);
}
