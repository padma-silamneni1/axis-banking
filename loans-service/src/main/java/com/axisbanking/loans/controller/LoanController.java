package com.axisbanking.loans.controller;

import com.axisbanking.loans.dto.LoanDto;
import com.axisbanking.loans.dto.ResponseDto;
import com.axisbanking.loans.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDto> applyForLoan(@Valid @RequestBody LoanDto loanDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.applyForLoan(loanDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/number/{loanNumber}")
    public ResponseEntity<LoanDto> getLoanByNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(loanService.getLoanByNumber(loanNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanDto>> getLoansByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(loanService.getLoansByCustomerId(customerId));
    }

    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LoanDto> approveLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<LoanDto> closeLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.closeLoan(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok(new ResponseDto("200", "Loan deleted successfully"));
    }
}
