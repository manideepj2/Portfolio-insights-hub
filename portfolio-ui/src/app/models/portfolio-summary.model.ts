import { AssetAllocation } from './asset-allocation.model';

export interface PortfolioSummary {

  portfolioId: string;

  totalPortfolioValue: number;

  instrumentCount: number;

  assetAllocation: AssetAllocation[];
}