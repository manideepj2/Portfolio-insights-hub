package com.portfolio.app.service;

import com.portfolio.app.dto.AssetAllocation;
import com.portfolio.app.dto.PortfolioSummaryResponse;
import com.portfolio.app.repository.HoldingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortfolioSummaryService {

    private final HoldingsRepository holdingsRepository;

    public PortfolioSummaryResponse getPortfolioSummary(String portfolioId) {

        ScanResponse scanResponse =
                holdingsRepository.scanAllHoldings();

        double totalValue = 0;

        int instrumentCount = 0;

        Map<String, Double> assetTotals = new HashMap<>();

        for (Map<String, AttributeValue> item : scanResponse.items()) {

            String currentPortfolioId =
                    item.get("portfolioId").s();

            if (!portfolioId.equals(currentPortfolioId)) {
                continue;
            }

            instrumentCount++;

            double marketValue =
                    Double.parseDouble(item.get("marketValue").n());

            totalValue += marketValue;

            String assetType =
                    item.get("assetType").s();

            assetTotals.put(
                    assetType,
                    assetTotals.getOrDefault(assetType, 0.0) + marketValue
            );
        }

        List<AssetAllocation> allocations = new ArrayList<>();

        for (Map.Entry<String, Double> entry : assetTotals.entrySet()) {

            allocations.add(
                    AssetAllocation.builder()
                            .assetType(entry.getKey())
                            .percentage(
                                    (entry.getValue() / totalValue) * 100
                            )
                            .build()
            );
        }

        return PortfolioSummaryResponse.builder()
                .portfolioId(portfolioId)
                .totalPortfolioValue(totalValue)
                .instrumentCount(instrumentCount)
                .assetAllocation(allocations)
                .build();
    }
}