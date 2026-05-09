package com.portfolio.app.service;

import com.portfolio.app.repository.HoldingsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioMetadataService {

    private final HoldingsRepository repository;

    public List<String> getInvestors() {

        ScanResponse response =
                repository.scanAll();

        Set<String> investors =
                new HashSet<>();

        for (Map<String, AttributeValue> item :
                response.items()) {

            investors.add(
                    item.get("investorId").s()
            );
        }

        return investors.stream().sorted().toList();
    }

    public List<String> getPortfolios() {

        ScanResponse response =
                repository.scanAll();

        Set<String> portfolios =
                new HashSet<>();

        for (Map<String, AttributeValue> item :
                response.items()) {

            portfolios.add(
                    item.get("portfolioId").s()
            );
        }

        return portfolios.stream().sorted().toList();
    }

    public List<String> getPortfoliosByInvestor(
            String investorId) {

        ScanResponse response =
                repository.scanAllByInvestor(
                        investorId
                );

        Set<String> portfolios =
                new HashSet<>();

        for (Map<String, AttributeValue> item :
                response.items()) {

            portfolios.add(
                    item.get("portfolioId").s()
            );
        }

        return portfolios.stream()
                .sorted()
                .toList();
    }
}