package com.portfolio.app.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.opencsv.CSVReader;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class PortfolioCsvProcessor implements RequestHandler<S3Event, String> {

    private final S3Client s3Client =
            S3Client.builder()
                    .region(Region.US_EAST_1)
                    .build();

    private final DynamoDbClient dynamoDbClient =
            DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .build();

    private static final String TABLE_NAME = "portfolio_holdings";

    @Override
    public String handleRequest(S3Event event, Context context) {

        try {

            String bucketName =
                    event.getRecords().get(0).getS3().getBucket().getName();

            String objectKey =
                    event.getRecords().get(0).getS3().getObject().getKey();

            context.getLogger().log("Processing file: " + objectKey);

            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build();

            ResponseInputStream<GetObjectResponse> s3Object =
                    s3Client.getObject(getObjectRequest);

            CSVReader csvReader =
                    new CSVReader(new BufferedReader(new InputStreamReader(s3Object)));

            String[] line;

            boolean headerSkipped = false;

            while ((line = csvReader.readNext()) != null) {

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String investorId = line[0];
                String portfolioId = line[1];
                String instrument = line[2];
                String assetType = line[3];
                String quantity = line[4];
                String marketValue = line[5];

                Map<String, AttributeValue> item = new HashMap<>();

                item.put("pk",
                        AttributeValue.builder()
                                .s("INVESTOR#" + investorId)
                                .build());

                item.put("sk",
                        AttributeValue.builder()
                                .s("PORTFOLIO#" + portfolioId + "#INSTRUMENT#" + instrument)
                                .build());

                item.put("investorId",
                        AttributeValue.builder()
                                .s(investorId)
                                .build());

                item.put("portfolioId",
                        AttributeValue.builder()
                                .s(portfolioId)
                                .build());

                item.put("instrument",
                        AttributeValue.builder()
                                .s(instrument)
                                .build());

                item.put("assetType",
                        AttributeValue.builder()
                                .s(assetType)
                                .build());

                item.put("quantity",
                        AttributeValue.builder()
                                .n(quantity)
                                .build());

                item.put("marketValue",
                        AttributeValue.builder()
                                .n(marketValue)
                                .build());

                PutItemRequest putItemRequest =
                        PutItemRequest.builder()
                                .tableName(TABLE_NAME)
                                .item(item)
                                .build();

                dynamoDbClient.putItem(putItemRequest);

                context.getLogger().log("Inserted record for: " + instrument);
            }

            return "CSV processed successfully";

        } catch (Exception e) {

            context.getLogger().log("Error processing CSV: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }
}