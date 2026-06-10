package com.axisbanking.accounts.controller;

import com.axisbanking.accounts.dto.AccountDto;
import com.axisbanking.accounts.dto.ResponseDto;
import com.axisbanking.accounts.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id,
                                                     @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new ResponseDto("200", "Account deleted successfully"));
    }
}
