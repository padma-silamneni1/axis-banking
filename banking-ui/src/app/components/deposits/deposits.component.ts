import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DepositService } from '../../services/deposit.service';
import { Deposit } from '../../models/deposit.model';

@Component({
  selector: 'app-deposits',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './deposits.component.html',
  styleUrl: './deposits.component.scss'
})
export class DepositsComponent implements OnInit {
  deposits: Deposit[] = [];
  newDeposit: Deposit = { customerId: 0, accountNumber: '', depositType: 'FIXED_DEPOSIT', principalAmount: 0, tenureMonths: 12 };

  constructor(private depositService: DepositService) {}

  ngOnInit(): void { this.loadDeposits(); }

  loadDeposits(): void {
    this.depositService.getDeposits().subscribe({ next: (d) => this.deposits = d, error: (e) => console.error(e) });
  }

  createDeposit(): void {
    this.depositService.createDeposit(this.newDeposit).subscribe({ next: () => this.loadDeposits(), error: (e) => console.error(e) });
  }

  closeDeposit(id: number): void {
    this.depositService.closeDeposit(id).subscribe({ next: () => this.loadDeposits(), error: (e) => console.error(e) });
  }
}
