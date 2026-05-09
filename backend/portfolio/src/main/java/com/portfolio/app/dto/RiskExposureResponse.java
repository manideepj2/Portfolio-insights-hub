package com.portfolio.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RiskExposureResponse {
    private List<AssetAllocation> assetAllocations;
    private List<TopHolding> topHoldings;
}
