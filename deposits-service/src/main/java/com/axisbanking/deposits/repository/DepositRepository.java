package com.axisbanking.deposits.repository;

import com.axisbanking.deposits.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    Optional<Deposit> findByDepositNumber(String depositNumber);

    List<Deposit> findByCustomerId(Long customerId);

    List<Deposit> findByAccountNumber(String accountNumber);
}
