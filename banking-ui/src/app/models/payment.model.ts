export interface Payment {
  id?: number;
  transactionId?: string;
  customerId: number;
  fromAccount: string;
  toAccount: string;
  beneficiaryName?: string;
  beneficiaryIfsc?: string;
  paymentType: string;
  amount: number;
  remarks?: string;
  upiId?: string;
  status?: string;
  transactionDate?: string;
}
