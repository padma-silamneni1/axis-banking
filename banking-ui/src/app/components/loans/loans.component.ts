import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoanService } from '../../services/loan.service';
import { Loan } from '../../models/loan.model';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './loans.component.html',
  styleUrl: './loans.component.scss'
})
export class LoansComponent implements OnInit {
  loans: Loan[] = [];
  newLoan: Loan = { customerId: 0, loanType: 'HOME_LOAN', loanAmount: 0, tenureMonths: 60 };

  constructor(private loanService: LoanService) {}

  ngOnInit(): void { this.loadLoans(); }

  loadLoans(): void {
    this.loanService.getLoans().subscribe({ next: (l) => this.loans = l, error: (e) => console.error(e) });
  }

  applyForLoan(): void {
    this.loanService.applyForLoan(this.newLoan).subscribe({ next: () => this.loadLoans(), error: (e) => console.error(e) });
  }

  approveLoan(id: number): void {
    this.loanService.approveLoan(id).subscribe({ next: () => this.loadLoans(), error: (e) => console.error(e) });
  }

  closeLoan(id: number): void {
    this.loanService.closeLoan(id).subscribe({ next: () => this.loadLoans(), error: (e) => console.error(e) });
  }
}
