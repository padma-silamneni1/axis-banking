package com.axisbanking.investments.controller;

import com.axisbanking.investments.dto.InvestmentDto;
import com.axisbanking.investments.dto.ResponseDto;
import com.axisbanking.investments.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvestmentController {

    private final InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<InvestmentDto> createInvestment(@Valid @RequestBody InvestmentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(investmentService.createInvestment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentDto> getInvestmentById(@PathVariable Long id) {
        return ResponseEntity.ok(investmentService.getInvestmentById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InvestmentDto>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByCustomerId(customerId));
    }

    @GetMapping
    public ResponseEntity<List<InvestmentDto>> getAllInvestments() {
        return ResponseEntity.ok(investmentService.getAllInvestments());
    }

    @PutMapping("/{id}/redeem")
    public ResponseEntity<InvestmentDto> redeemInvestment(@PathVariable Long id) {
        return ResponseEntity.ok(investmentService.redeemInvestment(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestment(id);
        return ResponseEntity.ok(new ResponseDto("200", "Investment deleted successfully"));
    }
}
