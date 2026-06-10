package com.axisbanking.insurance.controller;

import com.axisbanking.insurance.dto.InsurancePolicyDto;
import com.axisbanking.insurance.dto.ResponseDto;
import com.axisbanking.insurance.service.InsurancePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;

    @PostMapping
    public ResponseEntity<InsurancePolicyDto> createPolicy(@Valid @RequestBody InsurancePolicyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.createPolicy(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDto> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InsurancePolicyDto>> getPoliciesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(policyService.getPoliciesByCustomerId(customerId));
    }

    @GetMapping
    public ResponseEntity<List<InsurancePolicyDto>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<InsurancePolicyDto> cancelPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.cancelPolicy(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok(new ResponseDto("200", "Insurance policy deleted successfully"));
    }
}
