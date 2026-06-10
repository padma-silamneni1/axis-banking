import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Deposit } from '../models/deposit.model';

@Injectable({ providedIn: 'root' })
export class DepositService {

  private baseUrl = `${environment.apiGatewayUrl}/api/deposits`;

  constructor(private http: HttpClient) {}

  createDeposit(deposit: Deposit): Observable<Deposit> {
    return this.http.post<Deposit>(this.baseUrl, deposit);
  }

  getDeposits(): Observable<Deposit[]> {
    return this.http.get<Deposit[]>(this.baseUrl);
  }

  getDepositById(id: number): Observable<Deposit> {
    return this.http.get<Deposit>(`${this.baseUrl}/${id}`);
  }

  getDepositsByCustomer(customerId: number): Observable<Deposit[]> {
    return this.http.get<Deposit[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  closeDeposit(id: number): Observable<Deposit> {
    return this.http.put<Deposit>(`${this.baseUrl}/${id}/close`, {});
  }
}
