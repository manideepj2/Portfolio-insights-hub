import {
  Component,
  effect,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { PortfolioApiService }
from '../../core/services/portfolio-api';


import { Holding }
from '../../models/holding.model';
import { PortfolioStateService } from '../../core/services/portfolio-state.service';
import { GraphqlService, HoldingsQueryResponse } from '../../core/services/graphql.service';

@Component({
  selector: 'app-holdings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './holdings.html',
  styleUrl: './holdings.css'
})
export class HoldingsComponent {

  private graphqlService = inject(GraphqlService);

  private portfolioState = inject(PortfolioStateService);

  holdings = signal<Holding[]>([]);

  isLoading = signal(true);

  constructor() {

    effect(() => {

      const investorId =
        this.portfolioState
          .selectedInvestorId();

      if (!investorId) {
        return;
      }

      this.loadHoldings(investorId);
    });
  }

  loadHoldings(investorId: string): void {

    this.isLoading.set(true);

    this.graphqlService
      .getHoldings(investorId)
      .subscribe(({ data }) => {

        const response =
          data as HoldingsQueryResponse;

        this.holdings.set(
          response.holdings
        );

        this.isLoading.set(false);
      });
  }
}