package com.portfolio.app.controller;

import com.portfolio.app.dto.PortfolioSummaryResponse;
import com.portfolio.app.service.PortfolioSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioSummaryController {

    private final PortfolioSummaryService portfolioSummaryService;

    @GetMapping("/{portfolioId}/summary")
    public PortfolioSummaryResponse getSummary(
            @PathVariable String portfolioId) {

        return portfolioSummaryService
                .getPortfolioSummary(portfolioId);
    }
}