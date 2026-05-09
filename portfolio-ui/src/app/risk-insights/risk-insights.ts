import { MockedResponse } from './../../../node_modules/@apollo/client/testing/core/types/deprecated.d';
import { Component, effect, inject, input, OnInit, signal } from '@angular/core';
import {
  GraphqlService,
  RiskExposureQueryResponse,
  RiskExposureResponse,
} from '../core/services/graphql.service';
import { PortfolioStateService } from '../core/services/portfolio-state.service';

@Component({
  selector: 'app-risk-insights',
  imports: [],
  templateUrl: './risk-insights.html',
  styleUrl: './risk-insights.css',
})
export class RiskInsights {
  selectedInvestor = input.required<string>();
  selectedPortfolio = input.required<string>();

  private graphqlService = inject(GraphqlService);
  riskExposure = signal<RiskExposureResponse | null>(null);

  constructor() {
    effect(() => {
      if (this.selectedInvestor() && this.selectedPortfolio()) {
        this.loadRiskExposure();
      }
    });
  }

  loadRiskExposure(): void {
    this.graphqlService
      .getRiskExposure(this.selectedInvestor(), this.selectedPortfolio())
      .subscribe(({ data }) => {
        const response = data as RiskExposureQueryResponse;

        this.riskExposure.set(response.riskExposure);
      });
  }
}
