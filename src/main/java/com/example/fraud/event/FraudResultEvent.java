package com.example.fraud.event;


import jakarta.persistence.*;
import lombok.Data;


import java.time.Instant;
@Data
public class FraudResultEvent {
    private String transactionId;
    private String decision;      // "FRAUD" or "APPROVED"
    private String reason;        // e.g. "AMOUNT_EXCEEDS_LIMIT"
    private Double score;         // optional for later ML
    private Instant evaluatedAt;
}