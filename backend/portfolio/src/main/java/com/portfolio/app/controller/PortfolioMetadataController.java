package com.portfolio.app.controller;

import com.portfolio.app.service.PortfolioMetadataService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PortfolioMetadataController {

    private final PortfolioMetadataService service;

    @GetMapping("/investors")
    public List<String> getInvestors() {

        return service.getInvestors();
    }

    @GetMapping(
            "/investors/{investorId}/portfolios"
    )
    public List<String> getPortfoliosByInvestor(
            @PathVariable String investorId) {

        return service.getPortfoliosByInvestor(
                investorId
        );
    }

    @GetMapping("/portfolios")
    public List<String> getPortfolios() {

        return service.getPortfolios();
    }
}