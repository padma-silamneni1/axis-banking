import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  services = [
    { title: 'Accounts', description: 'Manage savings, current & salary accounts', route: '/accounts', icon: 'account_balance', color: '#97144d' },
    { title: 'Deposits', description: 'Fixed deposits & recurring deposits', route: '/deposits', icon: 'savings', color: '#e65100' },
    { title: 'Cards', description: 'Credit, debit & prepaid cards', route: '/cards', icon: 'credit_card', color: '#1565c0' },
    { title: 'Loans', description: 'Home, personal, auto & education loans', route: '/loans', icon: 'request_quote', color: '#2e7d32' },
    { title: 'Investments', description: 'Mutual funds, SIPs, stocks & bonds', route: '/investments', icon: 'trending_up', color: '#6a1b9a' },
    { title: 'Insurance', description: 'Life, health & vehicle insurance', route: '/insurance', icon: 'health_and_safety', color: '#00838f' },
    { title: 'Payments', description: 'UPI, NEFT, RTGS & bill payments', route: '/payments', icon: 'payments', color: '#ef6c00' }
  ];
}
