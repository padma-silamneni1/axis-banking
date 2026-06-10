package com.axisbanking.deposits.service;

import com.axisbanking.deposits.dto.DepositDto;
import java.util.List;

public interface DepositService {

    DepositDto createDeposit(DepositDto depositDto);

    DepositDto getDepositById(Long id);

    DepositDto getDepositByNumber(String depositNumber);

    List<DepositDto> getDepositsByCustomerId(Long customerId);

    List<DepositDto> getAllDeposits();

    DepositDto closeDeposit(Long id);

    void deleteDeposit(Long id);
}
