import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { InsurancePolicy } from '../models/insurance.model';

@Injectable({ providedIn: 'root' })
export class InsuranceService {

  private baseUrl = `${environment.apiGatewayUrl}/api/insurance`;

  constructor(private http: HttpClient) {}

  createPolicy(policy: InsurancePolicy): Observable<InsurancePolicy> {
    return this.http.post<InsurancePolicy>(this.baseUrl, policy);
  }

  getPolicies(): Observable<InsurancePolicy[]> {
    return this.http.get<InsurancePolicy[]>(this.baseUrl);
  }

  getPoliciesByCustomer(customerId: number): Observable<InsurancePolicy[]> {
    return this.http.get<InsurancePolicy[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  cancelPolicy(id: number): Observable<InsurancePolicy> {
    return this.http.put<InsurancePolicy>(`${this.baseUrl}/${id}/cancel`, {});
  }
}
