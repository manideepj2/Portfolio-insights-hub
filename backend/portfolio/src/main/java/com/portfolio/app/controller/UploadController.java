package com.portfolio.app.controller;

import com.portfolio.app.dto.UploadUrlResponse;
import com.portfolio.app.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final S3UploadService s3UploadService;

    @PostMapping("/presigned-url")
    public UploadUrlResponse generateUploadUrl() {
        return s3UploadService.generateUploadUrl();
    }
}