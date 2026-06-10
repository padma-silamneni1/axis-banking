export interface Loan {
  id?: number;
  loanNumber?: string;
  customerId: number;
  loanType: string;
  loanAmount: number;
  interestRate?: number;
  tenureMonths: number;
  emiAmount?: number;
  outstandingAmount?: number;
  totalInterest?: number;
  disbursementDate?: string;
  endDate?: string;
  status?: string;
}
