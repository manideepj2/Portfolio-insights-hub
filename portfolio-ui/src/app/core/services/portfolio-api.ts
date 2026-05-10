import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { UploadUrlResponse } from '../../models/uploadurl-response.model';
import { Holding } from '../../models/holding.model';
import { PortfolioSummary } from '../../models/portfolio-summary.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PortfolioApiService {
  private http = inject(HttpClient);

  private baseUrl = environment.apiBaseUrl;

  getHoldings(investorId: string): Observable<Holding[]> {
    return this.http.get<Holding[]>(`${this.baseUrl}/investor/${investorId}/holdings`);
  }

  getInvestors() {
    return this.http.get<string[]>(`${this.baseUrl}/investors`);
  }

  getPortfoliosByInvestor(investorId: string) {
    return this.http.get<string[]>(`${this.baseUrl}/investors/${investorId}/portfolios`);
  }
  getUploadUrl() {
    return this.http.post<UploadUrlResponse>(`${this.baseUrl}/uploads/presigned-url`, {});
  }

  getPortfolioSummary(portfolioId: string): Observable<PortfolioSummary> {
    return this.http.get<PortfolioSummary>(`${this.baseUrl}/portfolio/${portfolioId}/summary`);
  }
}
