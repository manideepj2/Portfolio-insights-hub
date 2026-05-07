package com.portfolio.app.service;

import com.portfolio.app.dto.HoldingResponse;
import com.portfolio.app.repository.HoldingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HoldingsService {

    private final HoldingsRepository holdingsRepository;

    public List<HoldingResponse> getHoldings(String investorId) {

        QueryResponse queryResponse =
                holdingsRepository.getHoldingsByInvestor(investorId);

        List<HoldingResponse> holdings = new ArrayList<>();

        for (Map<String, AttributeValue> item : queryResponse.items()) {

            holdings.add(
                    HoldingResponse.builder()
                            .investorId(item.get("investorId").s())
                            .portfolioId(item.get("portfolioId").s())
                            .instrument(item.get("instrument").s())
                            .assetType(item.get("assetType").s())
                            .quantity(Double.valueOf(item.get("quantity").n()))
                            .marketValue(Double.valueOf(item.get("marketValue").n()))
                            .build()
            );
        }

        return holdings;
    }
}