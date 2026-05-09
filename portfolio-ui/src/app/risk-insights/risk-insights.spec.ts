import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RiskInsights } from './risk-insights';

describe('RiskInsights', () => {
  let component: RiskInsights;
  let fixture: ComponentFixture<RiskInsights>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RiskInsights],
    }).compileComponents();

    fixture = TestBed.createComponent(RiskInsights);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
