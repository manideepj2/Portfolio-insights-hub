package com.portfolio.app.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HoldingsRepository {

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "portfolio_holdings";

    public QueryResponse getHoldingsByInvestor(String investorId) {

        Map<String, AttributeValue> expressionValues = new HashMap<>();

        expressionValues.put(":pk",
                AttributeValue.builder()
                        .s("INVESTOR#" + investorId)
                        .build());

        QueryRequest queryRequest =
                QueryRequest.builder()
                        .tableName(TABLE_NAME)
                        .keyConditionExpression("pk = :pk")
                        .expressionAttributeValues(expressionValues)
                        .build();

        return dynamoDbClient.query(queryRequest);
    }

    public ScanResponse scanAllHoldings() {

        ScanRequest scanRequest =
                ScanRequest.builder()
                        .tableName(TABLE_NAME)
                        .build();

        return dynamoDbClient.scan(scanRequest);
    }
}