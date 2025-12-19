package com.example.fraud.entity;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fraud_results")
public class FraudResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private Result result;

    private String ruleTriggered;
    private Double score;
    private Instant evaluatedAt;
    public enum Result {
        PASS,
        FRAUD
    }


    
}
