import { Component, OnInit, inject, signal } from '@angular/core';

import { CommonModule } from '@angular/common';

import { UploadComponent } from '../upload/upload';

import { HoldingsComponent } from '../holdings/holdings';

import { PortfolioSummaryComponent } from '../portfolio-summary/portfolio-summary';

import { PortfolioApiService } from '../../core/services/portfolio-api';
import { PortfolioStateService } from '../../core/services/portfolio-state.service';
import { RiskInsights } from '../../risk-insights/risk-insights';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    UploadComponent,
    HoldingsComponent,
    PortfolioSummaryComponent,
    RiskInsights,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent implements OnInit {
  private apiService = inject(PortfolioApiService);

  private portfolioState = inject(PortfolioStateService);

  investors = signal<string[]>([]);

  portfolios = signal<string[]>([]);
  selectedPortfolio: string = '';
  selectedInvestor: string = '';

  ngOnInit(): void {
    this.loadInvestors();
  }

  loadInvestors(): void {
    this.apiService.getInvestors().subscribe((response) => {
      this.investors.set(response);

      if (response.length > 0) {
        const firstInvestor = response[0];

        this.portfolioState.selectedInvestorId.set(firstInvestor);
        this.selectedInvestor = firstInvestor;

        this.loadPortfolios(firstInvestor);
      }
    });
  }

  loadPortfolios(investorId: string): void {
    this.apiService.getPortfoliosByInvestor(investorId).subscribe((response) => {
      this.portfolios.set(response);

      if (response.length > 0) {
        this.portfolioState.selectedPortfolioId.set(response[0]);
        this.selectedPortfolio = response[0];
      }
    });
  }

  changeInvestor(event: Event): void {
    const investorId = (event.target as HTMLSelectElement).value;

    this.portfolioState.selectedInvestorId.set(investorId);
    this.selectedInvestor = investorId;

    this.loadPortfolios(investorId);
  }

  changePortfolio(event: Event): void {
    const portfolioId = (event.target as HTMLSelectElement).value;

    this.portfolioState.selectedPortfolioId.set(portfolioId);
    this.selectedPortfolio = portfolioId;
  }
}
