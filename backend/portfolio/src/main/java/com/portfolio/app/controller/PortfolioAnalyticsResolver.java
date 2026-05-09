package com.portfolio.app.controller;

import com.portfolio.app.dto.RiskExposureResponse;
import com.portfolio.app.service.PortfolioAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PortfolioAnalyticsResolver {

    private final PortfolioAnalyticsService analyticsService;

    @QueryMapping
    public RiskExposureResponse riskExposure(
            @Argument String investorId,
            @Argument String portfolioId) {

        return analyticsService.getRiskExposure(investorId,portfolioId);
    }
}
