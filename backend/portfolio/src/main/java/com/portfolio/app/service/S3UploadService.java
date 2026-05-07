package com.portfolio.app.service;

import com.portfolio.app.dto.UploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public UploadUrlResponse generateUploadUrl() {

        String fileKey = "uploads/" + UUID.randomUUID() + ".csv";

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileKey)
                        .contentType("text/csv")
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(putObjectRequest)
                        .build();

        String uploadUrl =
                s3Presigner
                        .presignPutObject(presignRequest)
                        .url()
                        .toString();

        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .fileKey(fileKey)
                .build();
    }
}