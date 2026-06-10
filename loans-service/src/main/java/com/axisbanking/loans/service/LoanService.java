package com.axisbanking.loans.service;

import com.axisbanking.loans.dto.LoanDto;
import java.util.List;

public interface LoanService {

    LoanDto applyForLoan(LoanDto loanDto);

    LoanDto getLoanById(Long id);

    LoanDto getLoanByNumber(String loanNumber);

    List<LoanDto> getLoansByCustomerId(Long customerId);

    List<LoanDto> getAllLoans();

    LoanDto approveLoan(Long id);

    LoanDto closeLoan(Long id);

    void deleteLoan(Long id);
}
