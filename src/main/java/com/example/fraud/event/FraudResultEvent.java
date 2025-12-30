package com.example.fraud.event;

import java.time.Instant;

/**
 * Simple POJO for Kafka/SNS payloads describing the fraud check result.
 * Lombok was previously used but the build was failing to generate the
 * accessors, so we keep the class explicit to avoid annotation processing issues.
 */
public class FraudResultEvent {
    private String transactionId;
    private String decision;      // "FRAUD" or "APPROVED"
    private String reason;        // e.g. "AMOUNT_EXCEEDS_LIMIT"
    private Float score;          // optional for later ML
    private Instant evaluatedAt;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Instant getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Instant evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
