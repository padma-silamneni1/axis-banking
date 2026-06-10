import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Payment } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {

  private baseUrl = `${environment.apiGatewayUrl}/api/payments`;

  constructor(private http: HttpClient) {}

  initiatePayment(payment: Payment): Observable<Payment> {
    return this.http.post<Payment>(this.baseUrl, payment);
  }

  getPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.baseUrl);
  }

  getPaymentsByCustomer(customerId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  getPaymentByTransaction(transactionId: string): Observable<Payment> {
    return this.http.get<Payment>(`${this.baseUrl}/transaction/${transactionId}`);
  }
}
