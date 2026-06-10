export interface Deposit {
  id?: number;
  depositNumber?: string;
  customerId: number;
  accountNumber: string;
  depositType: string;
  principalAmount: number;
  interestRate?: number;
  tenureMonths: number;
  maturityAmount?: number;
  startDate?: string;
  maturityDate?: string;
  monthlyInstallment?: number;
  status?: string;
}
