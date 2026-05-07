# Portfolio Insights Hub — Low Level Design (LLD)

# 1. Introduction

This document provides the low-level technical design for the Portfolio Insights Hub platform.

The platform enables ingestion, processing, storage, querying, and visualization of investor portfolio holdings data using a cloud-native AWS-based architecture.

---

# 2. Service Decomposition

The application is decomposed into multiple logical services to improve scalability, maintainability, and separation of concerns.

---

# 2.1 Portfolio Service

## Responsibilities

- Portfolio holdings retrieval
- Portfolio summary aggregation
- Risk exposure calculations
- DynamoDB persistence access
- REST API exposure

## Technology Stack

- Java 17
- Spring Boot 3
- Spring Web
- AWS SDK v2

## Deployment

- ECS Fargate

---

# 2.2 GraphQL Gateway

## Responsibilities

- GraphQL schema exposure
- Query aggregation
- Resolver orchestration
- Unified data retrieval abstraction

## Technology Stack

- Spring GraphQL
- Java 17

## Deployment

- ECS Fargate

---

# 2.3 CSV Processing Lambda

## Responsibilities

- Process uploaded CSV files
- Validate file structure
- Validate records
- Detect duplicate uploads
- Transform holdings data
- Persist records into DynamoDB
- Log malformed records

## Technology Stack

- AWS Lambda
- Java
- OpenCSV

---

# 2.4 Angular Dashboard

## Responsibilities

- Upload CSV files
- Display portfolio summaries
- Render holdings tables
- Render risk visualizations
- Integrate REST and GraphQL APIs

## Technology Stack

- Angular
- Angular Material
- RxJS
- Apollo GraphQL
- Chart.js

---

# 3. Detailed Data Flow

# 3.1 Upload Flow

```text
User Uploads CSV
        ↓
Angular UI
        ↓
Generate Pre-Signed URL API
        ↓
S3 Upload
        ↓
S3 Event Notification
        ↓
Lambda Trigger
        ↓
CSV Validation & Transformation
        ↓
DynamoDB Persistence
        ↓
CloudWatch Logging
```
