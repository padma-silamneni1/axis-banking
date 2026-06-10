package com.axisbanking.payments.service;

import com.axisbanking.payments.dto.PaymentDto;
import java.util.List;

public interface PaymentService {

    PaymentDto initiatePayment(PaymentDto paymentDto);

    PaymentDto getPaymentById(Long id);

    PaymentDto getPaymentByTransactionId(String transactionId);

    List<PaymentDto> getPaymentsByCustomerId(Long customerId);

    List<PaymentDto> getAllPayments();
}
