package com.portfolio.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HoldingResponse {

    private String investorId;
    private String portfolioId;
    private String instrument;
    private String assetType;
    private Double quantity;
    private Double marketValue;
}