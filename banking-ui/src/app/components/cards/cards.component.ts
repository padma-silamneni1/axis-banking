import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardService } from '../../services/card.service';
import { Card } from '../../models/card.model';

@Component({
  selector: 'app-cards',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cards.component.html',
  styleUrl: './cards.component.scss'
})
export class CardsComponent implements OnInit {
  cards: Card[] = [];
  newCard: Card = { customerId: 0, cardHolderName: '', cardType: 'CREDIT_CARD' };

  constructor(private cardService: CardService) {}

  ngOnInit(): void { this.loadCards(); }

  loadCards(): void {
    this.cardService.getCards().subscribe({ next: (c) => this.cards = c, error: (e) => console.error(e) });
  }

  issueCard(): void {
    this.cardService.issueCard(this.newCard).subscribe({ next: () => this.loadCards(), error: (e) => console.error(e) });
  }

  blockCard(id: number): void {
    this.cardService.blockCard(id).subscribe({ next: () => this.loadCards(), error: (e) => console.error(e) });
  }

  activateCard(id: number): void {
    this.cardService.activateCard(id).subscribe({ next: () => this.loadCards(), error: (e) => console.error(e) });
  }
}
