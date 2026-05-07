# Portfolio Insights Hub — High Level Design (HLD)

## 1. Introduction

Portfolio Insights Hub is a cloud-native internal platform designed for Vanguard investment advisors and portfolio analysts to upload, process, analyze, and visualize investor portfolio holdings data.

The platform provides scalable portfolio ingestion pipelines, portfolio analytics APIs, GraphQL-based aggregation capabilities, and lightweight UI visualizations for portfolio insights and risk exposure analysis.

The solution is designed with extensibility in mind to support future analytics workloads, event-driven evolution, and AI-driven recommendation capabilities.

---

# 2. Objectives

The primary objectives of the platform are:

- Enable portfolio holdings CSV uploads
- Process and validate uploaded portfolio data
- Store holdings data in a scalable datastore
- Provide portfolio insights APIs
- Support flexible querying through GraphQL
- Visualize portfolio summaries and risk insights
- Ensure scalability, resilience, and maintainability

---

# 3. Functional Scope

The platform supports the following business capabilities:

## Portfolio Upload

- Upload CSV files containing investor portfolio holdings
- Store uploaded files in AWS S3

## Portfolio Processing

- Automatically trigger processing after upload
- Validate uploaded records
- Handle malformed records
- Persist transformed holdings into DynamoDB

## Portfolio Insights APIs

- Retrieve portfolio summaries
- Retrieve holdings by investor
- Analyze risk exposure and allocation

## GraphQL Layer

- Provide flexible aggregated querying capabilities
- Support future API evolution

## Angular Dashboard

- Upload CSV files
- Display portfolio metrics
- Visualize asset allocations
- Display holdings and risk insights

---

# 4. High-Level Architecture

```text
+----------------------+
| Angular Dashboard    |
+----------+-----------+
           |
           v
+----------------------+
| API Gateway          |
+----------+-----------+
           |
    -----------------------
    |                     |
    v                     v
+----------------+   +------------------+
| Portfolio API  |   | GraphQL Gateway  |
| Spring Boot    |   | Spring GraphQL   |
| ECS Fargate    |   | ECS Fargate      |
+--------+-------+   +--------+---------+
         |                     |
         -----------+-----------
                    |
                    v
             +-------------+
             | DynamoDB    |
             +-------------+

Upload Processing Flow
----------------------

Angular UI / Client
        |
        v
Pre-Signed Upload API
        |
        v
S3 Bucket
        |
        v
Lambda Trigger
        |
        v
CSV Processing Lambda
        |
        +----> Validation
        +----> Transformation
        +----> Duplicate Detection
        +----> Failure Logging
        |
        v
DynamoDB
```
