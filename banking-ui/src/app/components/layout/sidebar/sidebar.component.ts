import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  menuItems = [
    { label: 'Dashboard', route: '/dashboard', icon: 'dashboard' },
    { label: 'Accounts', route: '/accounts', icon: 'account_balance' },
    { label: 'Deposits', route: '/deposits', icon: 'savings' },
    { label: 'Cards', route: '/cards', icon: 'credit_card' },
    { label: 'Loans', route: '/loans', icon: 'request_quote' },
    { label: 'Investments', route: '/investments', icon: 'trending_up' },
    { label: 'Insurance', route: '/insurance', icon: 'health_and_safety' },
    { label: 'Payments', route: '/payments', icon: 'payments' }
  ];
}
