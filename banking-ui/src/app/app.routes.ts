import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { DepositsComponent } from './components/deposits/deposits.component';
import { CardsComponent } from './components/cards/cards.component';
import { LoansComponent } from './components/loans/loans.component';
import { InvestmentsComponent } from './components/investments/investments.component';
import { InsuranceComponent } from './components/insurance/insurance.component';
import { PaymentsComponent } from './components/payments/payments.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: 'deposits', component: DepositsComponent },
  { path: 'cards', component: CardsComponent },
  { path: 'loans', component: LoansComponent },
  { path: 'investments', component: InvestmentsComponent },
  { path: 'insurance', component: InsuranceComponent },
  { path: 'payments', component: PaymentsComponent },
  { path: '**', redirectTo: 'dashboard' }
];
