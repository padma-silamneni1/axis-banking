export interface Customer {
  id?: number;
  name: string;
  email: string;
  mobileNumber: string;
  panNumber?: string;
  aadhaarNumber?: string;
  address?: string;
  dateOfBirth?: string;
}

export interface Account {
  id?: number;
  accountNumber?: string;
  accountType: string;
  branchCode: string;
  ifscCode?: string;
  balance?: number;
  status?: string;
  customerId: number;
  customerName?: string;
}
