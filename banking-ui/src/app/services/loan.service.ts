import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Loan } from '../models/loan.model';

@Injectable({ providedIn: 'root' })
export class LoanService {

  private baseUrl = `${environment.apiGatewayUrl}/api/loans`;

  constructor(private http: HttpClient) {}

  applyForLoan(loan: Loan): Observable<Loan> {
    return this.http.post<Loan>(this.baseUrl, loan);
  }

  getLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(this.baseUrl);
  }

  getLoansByCustomer(customerId: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  approveLoan(id: number): Observable<Loan> {
    return this.http.put<Loan>(`${this.baseUrl}/${id}/approve`, {});
  }

  closeLoan(id: number): Observable<Loan> {
    return this.http.put<Loan>(`${this.baseUrl}/${id}/close`, {});
  }
}
