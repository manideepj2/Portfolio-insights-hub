import {
  Injectable,
  signal
} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PortfolioStateService {

  selectedInvestorId = signal<string>('');

  selectedPortfolioId = signal<string>('');
}