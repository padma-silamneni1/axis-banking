package com.axisbanking.accounts.service;

import com.axisbanking.accounts.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto getAccountByNumber(String accountNumber);

    List<AccountDto> getAccountsByCustomerId(Long customerId);

    List<AccountDto> getAllAccounts();

    AccountDto updateAccount(Long id, AccountDto accountDto);

    void deleteAccount(Long id);
}
