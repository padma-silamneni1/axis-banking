import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Investment } from '../models/investment.model';

@Injectable({ providedIn: 'root' })
export class InvestmentService {

  private baseUrl = `${environment.apiGatewayUrl}/api/investments`;

  constructor(private http: HttpClient) {}

  createInvestment(investment: Investment): Observable<Investment> {
    return this.http.post<Investment>(this.baseUrl, investment);
  }

  getInvestments(): Observable<Investment[]> {
    return this.http.get<Investment[]>(this.baseUrl);
  }

  getInvestmentsByCustomer(customerId: number): Observable<Investment[]> {
    return this.http.get<Investment[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  redeemInvestment(id: number): Observable<Investment> {
    return this.http.put<Investment>(`${this.baseUrl}/${id}/redeem`, {});
  }
}
