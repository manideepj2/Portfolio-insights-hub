---

# decisions.md

```md id="5b7tw0"
# Portfolio Insights Hub — Technical Decisions

# 1. Introduction

This document outlines key technical decisions made for the Portfolio Insights Hub platform and explains the rationale behind each architectural and engineering choice.

---

# 2. Why ECS Instead of Lambda-Only APIs

## Decision

Use ECS Fargate for REST and GraphQL APIs instead of a Lambda-only architecture.

## Rationale

The platform includes:

- Long-running API workloads
- Aggregation-heavy portfolio calculations
- GraphQL resolver orchestration
- Stateful connection management

ECS provides:

- Better runtime stability
- Predictable scaling
- Easier container orchestration
- Simplified observability
- Reduced cold-start impact

Lambda remains appropriate for:

- Event-driven CSV processing
- Lightweight asynchronous workflows

---

# 3. Why DynamoDB

## Decision

Use DynamoDB as the primary persistence layer for portfolio holdings.

## Rationale

DynamoDB provides:

- Fully managed serverless scalability
- High throughput support
- Low operational overhead
- Fast key-value access patterns
- Flexible schema evolution

The platform requires:

- Rapid investor holdings retrieval
- Scalable aggregation support
- High ingestion throughput

DynamoDB aligns well with these access patterns.

---

# 4. DynamoDB Partition Key Strategy

## Decision

Use investor-centric partition keys.

## Design

```text
PK = INVESTOR#{investorId}
SK = PORTFOLIO#{portfolioId}#INSTRUMENT#{instrument}
```
