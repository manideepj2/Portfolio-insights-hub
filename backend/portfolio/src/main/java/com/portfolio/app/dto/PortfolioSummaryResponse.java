package com.portfolio.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PortfolioSummaryResponse {

    private String portfolioId;
    private Double totalPortfolioValue;
    private Integer instrumentCount;
    List<AssetAllocation> assetAllocation;
}