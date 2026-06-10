package com.axisbanking.deposits.controller;

import com.axisbanking.deposits.dto.DepositDto;
import com.axisbanking.deposits.dto.ResponseDto;
import com.axisbanking.deposits.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<DepositDto> createDeposit(@Valid @RequestBody DepositDto depositDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.createDeposit(depositDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositDto> getDepositById(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.getDepositById(id));
    }

    @GetMapping("/number/{depositNumber}")
    public ResponseEntity<DepositDto> getDepositByNumber(@PathVariable String depositNumber) {
        return ResponseEntity.ok(depositService.getDepositByNumber(depositNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DepositDto>> getDepositsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(depositService.getDepositsByCustomerId(customerId));
    }

    @GetMapping
    public ResponseEntity<List<DepositDto>> getAllDeposits() {
        return ResponseEntity.ok(depositService.getAllDeposits());
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<DepositDto> closeDeposit(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.closeDeposit(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteDeposit(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.ok(new ResponseDto("200", "Deposit deleted successfully"));
    }
}
