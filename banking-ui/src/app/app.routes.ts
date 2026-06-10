import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AccountsComponent } from './components/accounts/accounts.component';
import { DepositsComponent } from './components/deposits/deposits.component';
import { CardsComponent } from './components/cards/cards.component';
import { LoansComponent } from './components/loans/loans.component';
import { InvestmentsComponent } from './components/investments/investments.component';
import { InsuranceComponent } from './components/insurance/insurance.component';
import { PaymentsComponent } from './components/payments/payments.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'accounts', component: AccountsComponent, canActivate: [authGuard] },
  { path: 'deposits', component: DepositsComponent, canActivate: [authGuard] },
  { path: 'cards', component: CardsComponent, canActivate: [authGuard] },
  { path: 'loans', component: LoansComponent, canActivate: [authGuard] },
  { path: 'investments', component: InvestmentsComponent, canActivate: [authGuard] },
  { path: 'insurance', component: InsuranceComponent, canActivate: [authGuard] },
  { path: 'payments', component: PaymentsComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: 'dashboard' }
];
