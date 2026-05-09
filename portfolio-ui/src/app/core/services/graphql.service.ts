import { Injectable, inject } from '@angular/core';

import { Apollo, gql } from 'apollo-angular';
import { PortfolioSummary } from '../../models/portfolio-summary.model';
import { Holding } from '../../models/holding.model';

export interface PortfolioSummaryQueryResult {
  portfolioSummary: PortfolioSummary;
}

export interface HoldingsQueryResponse {
  holdings: Holding[];
}

export interface AssetAllocation {
  assetType: string;
  percentage: number;
}

export interface TopHolding {
  instrument: string;
  marketValue: number;
}

export interface RiskExposureResponse {
  assetAllocations: AssetAllocation[];
  topHoldings: TopHolding[];
}

export interface RiskExposureQueryResponse {
  riskExposure: RiskExposureResponse;
}

@Injectable({
  providedIn: 'root',
})
export class GraphqlService {
  private apollo = inject(Apollo);

  getPortfolioSummary(portfolioId: string) {
    return this.apollo.watchQuery<PortfolioSummaryQueryResult>({
      query: gql`
        query GetPortfolioSummary($portfolioId: String!) {
          portfolioSummary(portfolioId: $portfolioId) {
            portfolioId
            totalPortfolioValue
            instrumentCount
            assetAllocation {
              assetType
              percentage
            }
          }
        }
      `,
      variables: { portfolioId },
    }).valueChanges;
  }

  getHoldings(investorId: string) {
    return this.apollo.watchQuery<HoldingsQueryResponse>({
      query: gql`
        query GetHoldings($investorId: String!) {
          holdings(investorId: $investorId) {
            investorId
            portfolioId
            instrument
            assetType
            quantity
            marketValue
          }
        }
      `,
      variables: { investorId },
    }).valueChanges;
  }

  getRiskExposure(investorId: string, portfolioId: string) {
    return this.apollo.watchQuery<RiskExposureQueryResponse>({
      query: gql`
        query GetRiskExposure($investorId: String!, $portfolioId: String!) {
          riskExposure(investorId: $investorId, portfolioId: $portfolioId) {
            assetAllocations {
              assetType
              percentage
            }

            topHoldings {
              instrument
              marketValue
            }
          }
        }
      `,
      variables: {
        investorId,
        portfolioId,
      },
    }).valueChanges;
  }
}
