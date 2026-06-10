import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { Account, Customer } from '../../models/account.model';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.scss'
})
export class AccountsComponent implements OnInit {
  accounts: Account[] = [];
  customers: Customer[] = [];
  showCreateForm = false;

  newAccount: Account = {
    accountType: 'SAVINGS',
    branchCode: '',
    customerId: 0
  };

  newCustomer: Customer = {
    name: '',
    email: '',
    mobileNumber: ''
  };

  activeTab: 'accounts' | 'customers' = 'accounts';

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadAccounts();
    this.loadCustomers();
  }

  loadAccounts(): void {
    this.accountService.getAccounts().subscribe({
      next: (data) => this.accounts = data,
      error: (err) => console.error('Error loading accounts', err)
    });
  }

  loadCustomers(): void {
    this.accountService.getCustomers().subscribe({
      next: (data) => this.customers = data,
      error: (err) => console.error('Error loading customers', err)
    });
  }

  createAccount(): void {
    this.accountService.createAccount(this.newAccount).subscribe({
      next: () => {
        this.loadAccounts();
        this.showCreateForm = false;
      },
      error: (err) => console.error('Error creating account', err)
    });
  }

  createCustomer(): void {
    this.accountService.createCustomer(this.newCustomer).subscribe({
      next: () => {
        this.loadCustomers();
        this.newCustomer = { name: '', email: '', mobileNumber: '' };
      },
      error: (err) => console.error('Error creating customer', err)
    });
  }

  deleteAccount(id: number): void {
    this.accountService.deleteAccount(id).subscribe({
      next: () => this.loadAccounts(),
      error: (err) => console.error('Error deleting account', err)
    });
  }
}
