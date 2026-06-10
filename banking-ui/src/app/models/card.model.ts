export interface Card {
  id?: number;
  cardNumber?: string;
  customerId: number;
  cardHolderName: string;
  cardType: string;
  cardNetwork?: string;
  creditLimit?: number;
  availableLimit?: number;
  outstandingAmount?: number;
  expiryDate?: string;
  status?: string;
}
