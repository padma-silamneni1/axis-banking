import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Card } from '../models/card.model';

@Injectable({ providedIn: 'root' })
export class CardService {

  private baseUrl = `${environment.apiGatewayUrl}/api/cards`;

  constructor(private http: HttpClient) {}

  issueCard(card: Card): Observable<Card> {
    return this.http.post<Card>(this.baseUrl, card);
  }

  getCards(): Observable<Card[]> {
    return this.http.get<Card[]>(this.baseUrl);
  }

  getCardsByCustomer(customerId: number): Observable<Card[]> {
    return this.http.get<Card[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  blockCard(id: number): Observable<Card> {
    return this.http.put<Card>(`${this.baseUrl}/${id}/block`, {});
  }

  activateCard(id: number): Observable<Card> {
    return this.http.put<Card>(`${this.baseUrl}/${id}/activate`, {});
  }
}
