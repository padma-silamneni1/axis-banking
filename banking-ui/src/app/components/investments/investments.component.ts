import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InvestmentService } from '../../services/investment.service';
import { Investment } from '../../models/investment.model';

@Component({
  selector: 'app-investments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './investments.component.html',
  styleUrl: './investments.component.scss'
})
export class InvestmentsComponent implements OnInit {
  investments: Investment[] = [];
  newInvestment: Investment = { customerId: 0, investmentType: 'MUTUAL_FUND', schemeName: '', investedAmount: 0 };

  constructor(private investmentService: InvestmentService) {}

  ngOnInit(): void { this.loadInvestments(); }

  loadInvestments(): void {
    this.investmentService.getInvestments().subscribe({ next: (i) => this.investments = i, error: (e) => console.error(e) });
  }

  createInvestment(): void {
    this.investmentService.createInvestment(this.newInvestment).subscribe({ next: () => this.loadInvestments(), error: (e) => console.error(e) });
  }

  redeem(id: number): void {
    this.investmentService.redeemInvestment(id).subscribe({ next: () => this.loadInvestments(), error: (e) => console.error(e) });
  }
}
