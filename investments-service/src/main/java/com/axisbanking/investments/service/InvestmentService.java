package com.axisbanking.investments.service;

import com.axisbanking.investments.dto.InvestmentDto;
import java.util.List;

public interface InvestmentService {

    InvestmentDto createInvestment(InvestmentDto investmentDto);

    InvestmentDto getInvestmentById(Long id);

    List<InvestmentDto> getInvestmentsByCustomerId(Long customerId);

    List<InvestmentDto> getAllInvestments();

    InvestmentDto redeemInvestment(Long id);

    void deleteInvestment(Long id);
}
