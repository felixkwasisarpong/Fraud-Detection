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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public String getRuleTriggered() {
		return ruleTriggered;
	}
	public void setRuleTriggered(String ruleTriggered) {
		this.ruleTriggered = ruleTriggered;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Instant getEvaluatedAt() {
		return evaluatedAt;
	}
	public void setEvaluatedAt(Instant evaluatedAt) {
		this.evaluatedAt = evaluatedAt;
	}


    
}
