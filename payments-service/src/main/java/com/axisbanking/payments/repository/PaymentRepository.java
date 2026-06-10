package com.axisbanking.payments.repository;

import com.axisbanking.payments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByFromAccount(String fromAccount);
}
