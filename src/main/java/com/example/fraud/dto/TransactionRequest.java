package com.example.fraud.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
	

@Data
public class TransactionRequest {
	private String transactionId;
	
	@NotNull
	private  String accountId;
	
	@NotNull
	private String merchantId;
	
	@Positive
	private Double amount;
	
	private String currency  = "USD";

}
