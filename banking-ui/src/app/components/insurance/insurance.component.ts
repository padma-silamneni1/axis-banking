import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InsuranceService } from '../../services/insurance.service';
import { InsurancePolicy } from '../../models/insurance.model';

@Component({
  selector: 'app-insurance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './insurance.component.html',
  styleUrl: './insurance.component.scss'
})
export class InsuranceComponent implements OnInit {
  policies: InsurancePolicy[] = [];
  newPolicy: InsurancePolicy = { customerId: 0, insuranceType: 'LIFE_INSURANCE', policyName: '', sumAssured: 0, premiumAmount: 0 };

  constructor(private insuranceService: InsuranceService) {}

  ngOnInit(): void { this.loadPolicies(); }

  loadPolicies(): void {
    this.insuranceService.getPolicies().subscribe({ next: (p) => this.policies = p, error: (e) => console.error(e) });
  }

  createPolicy(): void {
    this.insuranceService.createPolicy(this.newPolicy).subscribe({ next: () => this.loadPolicies(), error: (e) => console.error(e) });
  }

  cancelPolicy(id: number): void {
    this.insuranceService.cancelPolicy(id).subscribe({ next: () => this.loadPolicies(), error: (e) => console.error(e) });
  }
}
