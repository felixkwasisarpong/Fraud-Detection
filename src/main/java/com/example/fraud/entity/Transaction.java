package com.example.fraud.entity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

@Id
private String transactionId;
private String accountId;
private String merchantId;
private Double amount;
private String currency;
private Instant timestamp;


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
		RECEIVED,
		PROCESSED,
		FAILED
	}
public String getTransactionId() {
    return transactionId;
}
public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
}
public String getAccountId() {
	return accountId;
}
public void setAccountId(String accountId) {
	this.accountId = accountId;
}
public String getMerchantId() {
	return merchantId;
}
public void setMerchantId(String merchantId) {
	this.merchantId = merchantId;
}
public Double getAmount() {
	return amount;
}
public void setAmount(Double amount) {
	this.amount = amount;
}
public String getCurrency() {
	return currency;
}
public void setCurrency(String currency) {
	this.currency = currency;
}
public Instant getTimestamp() {
	return timestamp;
}
public void setTimestamp(Instant timestamp) {
	this.timestamp = timestamp;
}





    
}
