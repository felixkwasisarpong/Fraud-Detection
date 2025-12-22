package com.example.fraud.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fraud_results")
public class FraudResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String transactionId;

	@Enumerated(EnumType.STRING)
	private Result result;

	private String reason;

	private Instant evaluatedAt;

	public enum Result {
		APPROVED,
		FRAUD
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Instant getEvaluatedAt() {
		return evaluatedAt;
	}

	public void setEvaluatedAt(Instant evaluatedAt) {
		this.evaluatedAt = evaluatedAt;
	}
// getters/setters
}