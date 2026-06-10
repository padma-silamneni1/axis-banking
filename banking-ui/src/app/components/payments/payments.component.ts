import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { Payment } from '../../models/payment.model';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payments.component.html',
  styleUrl: './payments.component.scss'
})
export class PaymentsComponent implements OnInit {
  payments: Payment[] = [];
  newPayment: Payment = { customerId: 0, fromAccount: '', toAccount: '', paymentType: 'UPI', amount: 0 };

  constructor(private paymentService: PaymentService) {}

  ngOnInit(): void { this.loadPayments(); }

  loadPayments(): void {
    this.paymentService.getPayments().subscribe({ next: (p) => this.payments = p, error: (e) => console.error(e) });
  }

  initiatePayment(): void {
    this.paymentService.initiatePayment(this.newPayment).subscribe({ next: () => this.loadPayments(), error: (e) => console.error(e) });
  }
}
