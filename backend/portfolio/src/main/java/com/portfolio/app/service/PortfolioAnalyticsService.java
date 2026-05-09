package com.portfolio.app.service;

import com.portfolio.app.dto.RiskExposureResponse;

public interface PortfolioAnalyticsService {

    RiskExposureResponse getRiskExposure(
            String investorId,
            String portfolioId);
}
