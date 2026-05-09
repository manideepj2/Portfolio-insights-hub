package com.portfolio.app.service;

import com.portfolio.app.dto.HoldingResponse;
import com.portfolio.app.dto.PortfolioSummaryResponse;
import com.portfolio.app.dto.RiskExposureResponse;
import com.portfolio.app.dto.TopHolding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioAnalyticsServiceImpl implements PortfolioAnalyticsService {

    private final PortfolioSummaryService portfolioService;

    private final HoldingsService holdingsService;

    @Override
    public RiskExposureResponse getRiskExposure(
            String investorId,
            String portfolioId) {

        PortfolioSummaryResponse summary =
                portfolioService.getPortfolioSummary(portfolioId);

        List<HoldingResponse> holdings =
                holdingsService.getHoldings(investorId);

        List<TopHolding> topHoldings =
                holdings.stream()
                        .sorted((a, b) ->
                                Double.compare(
                                        b.getMarketValue(),
                                        a.getMarketValue()))
                        .limit(5)
                        .map(holding ->
                                new TopHolding(
                                        holding.getInstrument(),
                                        holding.getMarketValue()))
                        .toList();

        return new RiskExposureResponse(
                summary.getAssetAllocation(),
                topHoldings);
    }
}
