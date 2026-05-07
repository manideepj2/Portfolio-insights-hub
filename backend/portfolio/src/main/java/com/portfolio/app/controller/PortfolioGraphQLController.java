package com.portfolio.app.graphql;

import com.portfolio.app.dto.HoldingResponse;
import com.portfolio.app.dto.PortfolioSummaryResponse;
import com.portfolio.app.service.HoldingsService;
import com.portfolio.app.service.PortfolioSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortfolioGraphQLController {

    private final HoldingsService holdingsService;

    private final PortfolioSummaryService portfolioSummaryService;

    @QueryMapping
    public List<HoldingResponse> holdings(
            @Argument String investorId) {

        return holdingsService.getHoldings(investorId);
    }

    @QueryMapping
    public PortfolioSummaryResponse portfolioSummary(
            @Argument String portfolioId) {

        return portfolioSummaryService
                .getPortfolioSummary(portfolioId);
    }
}