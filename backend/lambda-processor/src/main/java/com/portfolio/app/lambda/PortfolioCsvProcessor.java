package com.portfolio.app.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.opencsv.CSVReader;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioCsvProcessor
        implements RequestHandler<S3Event, String> {

    private final S3Client s3Client =
            S3Client.builder()
                    .region(Region.US_EAST_1)
                    .build();

    private final DynamoDbClient dynamoDbClient =
            DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .build();

    private static final String TABLE_NAME =
            "portfolio_holdings";

    @Override
    public String handleRequest(
            S3Event event,
            Context context) {

        try {

            String bucketName =
                    event.getRecords()
                            .get(0)
                            .getS3()
                            .getBucket()
                            .getName();

            String objectKey =
                    event.getRecords()
                            .get(0)
                            .getS3()
                            .getObject()
                            .getKey();

            context.getLogger().log(
                    "Processing file: " + objectKey);

            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build();

            List<WriteRequest> writeRequests =
                    new ArrayList<>();

            try (

                    ResponseInputStream<GetObjectResponse> s3Object =
                            s3Client.getObject(getObjectRequest);

                    CSVReader csvReader =
                            new CSVReader(
                                    new BufferedReader(
                                            new InputStreamReader(s3Object)))

            ) {

                String[] line;

                boolean headerSkipped = false;

                int rowNumber = 0;

                while ((line = csvReader.readNext()) != null) {

                    rowNumber++;

                    /*
                     * Skip header
                     */
                    if (!headerSkipped) {

                        headerSkipped = true;
                        continue;
                    }

                    /*
                     * Skip empty rows
                     */
                    if (line.length == 0 ||
                            line[0].isBlank()) {

                        context.getLogger().log(
                                "Skipping empty row at line: "
                                        + rowNumber);

                        continue;
                    }

                    /*
                     * Validate column count
                     */
                    if (line.length < 6) {

                        context.getLogger().log(
                                "Malformed row at line: "
                                        + rowNumber);

                        continue;
                    }

                    String investorId = line[0];
                    String portfolioId = line[1];
                    String instrument = line[2];
                    String assetType = line[3];
                    String quantity = line[4];
                    String marketValue = line[5];

                    /*
                     * Numeric validation
                     */
                    try {

                        Double.parseDouble(quantity);
                        Double.parseDouble(marketValue);

                    } catch (Exception ex) {

                        context.getLogger().log(
                                "Invalid numeric values at line: "
                                        + rowNumber);

                        continue;
                    }

                    Map<String, AttributeValue> item =
                            new HashMap<>();

                    item.put(
                            "pk",
                            AttributeValue.builder()
                                    .s("INVESTOR#" + investorId)
                                    .build());

                    item.put(
                            "sk",
                            AttributeValue.builder()
                                    .s(
                                            "PORTFOLIO#"
                                                    + portfolioId
                                                    + "#INSTRUMENT#"
                                                    + instrument)
                                    .build());

                    item.put(
                            "investorId",
                            AttributeValue.builder()
                                    .s(investorId)
                                    .build());

                    item.put(
                            "portfolioId",
                            AttributeValue.builder()
                                    .s(portfolioId)
                                    .build());

                    item.put(
                            "instrument",
                            AttributeValue.builder()
                                    .s(instrument)
                                    .build());

                    item.put(
                            "assetType",
                            AttributeValue.builder()
                                    .s(assetType)
                                    .build());

                    item.put(
                            "quantity",
                            AttributeValue.builder()
                                    .n(quantity)
                                    .build());

                    item.put(
                            "marketValue",
                            AttributeValue.builder()
                                    .n(marketValue)
                                    .build());

                    item.put(
                            "uploadedFile",
                            AttributeValue.builder()
                                    .s(objectKey)
                                    .build());

                    WriteRequest writeRequest =
                            WriteRequest.builder()
                                    .putRequest(
                                            PutRequest.builder()
                                                    .item(item)
                                                    .build())
                                    .build();

                    writeRequests.add(writeRequest);

                    /*
                     * DynamoDB batch limit = 25
                     */
                    if (writeRequests.size() == 25) {

                        flushBatch(writeRequests, context);
                    }
                }

                /*
                 * Flush remaining records
                 */
                if (!writeRequests.isEmpty()) {

                    flushBatch(writeRequests, context);
                }
            }

            context.getLogger().log(
                    "CSV processing completed successfully");

            return "CSV processed successfully";

        } catch (Exception e) {

            context.getLogger().log(
                    "Error processing CSV: "
                            + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    private void flushBatch(
            List<WriteRequest> writeRequests,
            Context context) {

        Map<String, List<WriteRequest>> batchMap =
                new HashMap<>();

        batchMap.put(TABLE_NAME,
                new ArrayList<>(writeRequests));

        BatchWriteItemRequest batchRequest =
                BatchWriteItemRequest.builder()
                        .requestItems(batchMap)
                        .build();

        dynamoDbClient.batchWriteItem(batchRequest);

        context.getLogger().log(
                "Inserted batch of size: "
                        + writeRequests.size());

        writeRequests.clear();
    }
}