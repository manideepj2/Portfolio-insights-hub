import { Component, effect, inject, signal } from '@angular/core';

import { CommonModule } from '@angular/common';

import { PortfolioSummary } from '../../models/portfolio-summary.model';
import { PortfolioStateService } from '../../core/services/portfolio-state.service';
import { GraphqlService, PortfolioSummaryQueryResult } from '../../core/services/graphql.service';

@Component({
  selector: 'app-portfolio-summary',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './portfolio-summary.html',
  styleUrl: './portfolio-summary.css',
})
export class PortfolioSummaryComponent {
  private graphqlService = inject(GraphqlService);

  private portfolioState = inject(PortfolioStateService);

  summary = signal<PortfolioSummary | null>(null);

  isLoading = signal(true);

  constructor() {
    effect(() => {
      const portfolioId = this.portfolioState.selectedPortfolioId();

      if (!portfolioId) {
        return;
      }

      this.loadSummary(portfolioId);
    });
  }

  loadSummary(portfolioId: string): void {
    this.graphqlService.getPortfolioSummary(portfolioId).subscribe(({ data }) => {
      const response = data as PortfolioSummaryQueryResult;
      this.summary.set(response.portfolioSummary);
      this.isLoading.set(false);
    });
  }
}
