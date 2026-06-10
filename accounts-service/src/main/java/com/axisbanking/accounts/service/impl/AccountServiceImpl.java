package com.axisbanking.accounts.service.impl;

import com.axisbanking.accounts.dto.AccountDto;
import com.axisbanking.accounts.exception.ResourceNotFoundException;
import com.axisbanking.accounts.model.*;
import com.axisbanking.accounts.repository.AccountRepository;
import com.axisbanking.accounts.repository.CustomerRepository;
import com.axisbanking.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AccountDto createAccount(AccountDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId().toString()));

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .accountType(AccountType.valueOf(dto.getAccountType()))
                .branchCode(dto.getBranchCode())
                .ifscCode("AXIS0" + dto.getBranchCode())
                .balance(dto.getBalance() != null ? dto.getBalance() : BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .customer(customer)
                .build();

        Account saved = accountRepository.save(account);
        return mapToDto(saved);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id.toString()));
        return mapToDto(account);
    }

    @Override
    public AccountDto getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountNumber));
        return mapToDto(account);
    }

    @Override
    public List<AccountDto> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id.toString()));

        account.setAccountType(AccountType.valueOf(dto.getAccountType()));
        account.setBranchCode(dto.getBranchCode());
        if (dto.getStatus() != null) {
            account.setStatus(AccountStatus.valueOf(dto.getStatus()));
        }

        Account updated = accountRepository.save(account);
        return mapToDto(updated);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id.toString()));
        accountRepository.delete(account);
    }

    private AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .branchCode(account.getBranchCode())
                .ifscCode(account.getIfscCode())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .customerId(account.getCustomer().getId())
                .customerName(account.getCustomer().getName())
                .build();
    }

    private String generateAccountNumber() {
        Random random = new Random();
        long number = 9000000000L + (long)(random.nextDouble() * 1000000000L);
        String accountNumber = "AXIS" + number;
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            number = 9000000000L + (long)(random.nextDouble() * 1000000000L);
            accountNumber = "AXIS" + number;
        }
        return accountNumber;
    }
}
