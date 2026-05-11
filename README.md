# Portfolio Insights Hub

Cloud-native portfolio analytics platform built using Angular, Spring Boot, AWS Lambda, DynamoDB, ECS Fargate, S3, GraphQL, and AWS managed infrastructure.

The platform allows investment portfolio data ingestion through CSV uploads, automated processing using AWS Lambda, storage in DynamoDB, and visualization through a lightweight Angular dashboard.

---

# Architecture Overview

```text
Angular UI (S3 Static Hosting)
        ↓
Application Load Balancer (ALB)
        ↓
ECS Fargate Spring Boot APIs
        ↓
DynamoDB

CSV Upload → S3 Bucket
                ↓
         Lambda Trigger
                ↓
      CSV Processing Logic
                ↓
           DynamoDB
```

---

# Repository Structure

```text
repo/
├── portfolio-ui/
│
├── backend/
│   ├── portfolio-service/
│   └── lambda-processor/
```

---

# Modules

## 1. portfolio-ui

Angular frontend application.

### Responsibilities
- Upload CSV files
- Display portfolio summary
- Display holdings
- Display risk insights
- Call GraphQL and REST APIs
- Render investor portfolio dashboard

### Tech Stack
- Angular
- TypeScript
- Apollo GraphQL
- Bootstrap

### Features
- Dynamic investor selection
- Portfolio filtering
- Holdings table
- Portfolio summary metrics
- Risk allocation view

---

## 2. backend/portfolio-service

Spring Boot microservice deployed on ECS Fargate.

### Responsibilities
- Expose REST APIs
- Expose GraphQL APIs
- Query DynamoDB
- Aggregate portfolio insights
- Serve dashboard data

### Tech Stack
- Java 17
- Spring Boot
- GraphQL
- DynamoDB SDK v2
- Docker
- ECS Fargate

### APIs

#### Portfolio Summary
Returns:
- Total portfolio value
- Asset allocation
- Instrument counts

#### Holdings API
Returns:
- Holdings by investor and portfolio

#### Risk Exposure API
Returns:
- Asset allocation percentages
- Top concentrated holdings

---

## 3. backend/lambda-processor

AWS Lambda service triggered by S3 uploads.

### Responsibilities
- Process uploaded CSV files
- Validate records
- Parse portfolio holdings
- Persist transformed data into DynamoDB
- Handle malformed records
- Log ingestion failures

### Tech Stack
- Java 17
- AWS Lambda
- S3 Events
- DynamoDB
- OpenCSV

---

# AWS Services Used

| Service | Purpose |
|---|---|
| S3 | Angular hosting and CSV upload storage |
| Lambda | CSV ingestion and processing |
| DynamoDB | Portfolio holdings persistence |
| ECS Fargate | Containerized backend hosting |
| ECR | Docker image repository |
| ALB | Stable backend routing and load balancing |
| CloudWatch | Logs and monitoring |
| EC2/VPC | Networking, subnets, security groups |

---

# GraphQL

The backend exposes GraphQL APIs to support flexible dashboard querying.

### Example Queries

#### Portfolio Summary

```graphql
query {
  portfolioSummary(
    investorId: "INV101",
    portfolioId: "PF1001"
  ) {
    totalValue
    instrumentCount
    allocations {
      assetType
      percentage
    }
  }
}
```

#### Holdings Query

```graphql
query {
  holdings(
    investorId: "INV101",
    portfolioId: "PF1001"
  ) {
    instrument
    assetType
    quantity
    marketValue
  }
}
```

---

# DynamoDB Design

## Table Structure

### Partition Key

```text
investorId
```

### Sort Key

```text
portfolioId
```

### Additional Attributes

- instrument
- assetType
- quantity
- marketValue

---

# Local Setup

## Prerequisites

- Java 17
- Node.js
- Angular CLI
- Docker Desktop
- AWS CLI
- Maven

---

# Running Frontend Locally

```bash
cd portfolio-ui
npm install
ng serve
```

Frontend runs on:

```text
http://localhost:4200
```

---

# Running Backend Locally

```bash
cd backend/portfolio-service
mvn clean install
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

# Docker Build

## Backend Image

```bash
cd backend/portfolio-service
mvn clean package

docker build -t portfolio-service .
```

---

# ECR Push

```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

docker tag portfolio-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/portfolio:latest

docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/portfolio:latest
```

---

# ECS Deployment

## Deployment Flow

```text
Docker Image → ECR → ECS Task Definition → ECS Service → ALB
```

### ECS Components

| Component | Purpose |
|---|---|
| Cluster | Logical grouping of services |
| Service | Manages running tasks |
| Task Definition | Container blueprint |
| Task | Running container instance |
| Target Group | Tracks healthy ECS tasks |
| ALB | Routes traffic to ECS tasks |

---

# Security Group Design

## ALB Security Group

Allows:

```text
80 → 0.0.0.0/0
```

Meaning:
- Public internet can access ALB

## ECS Security Group

Allows:

```text
8080 → ALB Security Group
```

Meaning:
- Only ALB can access backend containers

---

# CI/CD Strategy

Monorepo path-based deployment strategy.

## Frontend Changes

```text
portfolio-ui/**
```

Triggers:
- Angular build
- S3 deployment

## Backend Changes

```text
backend/portfolio-service/**
```

Triggers:
- Maven build
- Docker build
- ECR push
- ECS deployment

## Lambda Changes

```text
backend/lambda-processor/**
```

Triggers:
- Lambda package build
- Lambda deployment

---

# Future Improvements

- HTTPS using ACM + ALB
- CloudFront CDN
- JWT Authentication
- Terraform IaC
- CI/CD using GitHub Actions
- Auto scaling policies
- AI-driven portfolio recommendations
- Event-driven streaming architecture
- Multi-region deployment
- Redis caching
- OpenSearch analytics

---

# Non-Functional Characteristics

- Scalable
- Fault tolerant
- Cloud-native
- Event-driven ingestion
- Containerized backend
- Serverless processing
- Modular architecture
- Production-oriented deployment

---

# Deployment Notes

The frontend is hosted using:

```text
Amazon S3 Static Website Hosting
```

The backend is deployed using:

```text
ECS Fargate + ALB
```

The ingestion flow is event-driven:

```text
S3 Upload → Lambda → DynamoDB
```

---

# Author

Portfolio Insights Hub Assignment

Technical Stack:
- Java 17
- Spring Boot
- Angular
- GraphQL
- AWS ECS
- AWS Lambda
- DynamoDB
- S3
- Docker
- CloudWatch

