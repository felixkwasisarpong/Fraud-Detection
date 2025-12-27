package com.example.fraud.controller;

import com.example.fraud.dto.TransactionRequest;
import com.example.fraud.dto.TransactionResponse;
import com.example.fraud.event.TransactionEvent;
import com.example.fraud.service.FraudScoringService;
import com.example.fraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.MDC;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	private final TransactionService  transactionService;
	private final FraudScoringService fraudScoringService;
	
	public TransactionController(TransactionService transactionService,FraudScoringService fraudScoringService) {
		this.transactionService = transactionService;
		this.fraudScoringService = fraudScoringService;
	}



	private void attachTrace(String transactionId) {
		MDC.put("transactionId", transactionId);
	}
	@PostMapping
	public ResponseEntity<TransactionResponse> receiveTransaction(@Valid @RequestBody TransactionRequest request){
		attachTrace(request.getTransactionId());
		TransactionResponse response = transactionService.receiveTransaction(request);
		return ResponseEntity.ok(response);
	}


	@PostMapping("/test-ml")
	public String testML(@RequestBody TransactionEvent event) throws Exception {
		float probability = fraudScoringService.score(new float[]{
				event.getAmount().floatValue(), 12, 3, 2, 1
		});
		return "Fraud probability = " + probability;
	}

}
