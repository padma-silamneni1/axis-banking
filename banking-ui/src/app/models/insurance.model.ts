export interface InsurancePolicy {
  id?: number;
  policyNumber?: string;
  customerId: number;
  insuranceType: string;
  policyName: string;
  sumAssured: number;
  premiumAmount: number;
  premiumFrequency?: string;
  startDate?: string;
  endDate?: string;
  nomineeName?: string;
  nomineeRelation?: string;
  status?: string;
}
