export interface Investment {
  id?: number;
  investmentNumber?: string;
  customerId: number;
  investmentType: string;
  schemeName: string;
  investedAmount: number;
  currentValue?: number;
  units?: number;
  navPrice?: number;
  sipAmount?: number;
  investmentDate?: string;
  status?: string;
}
