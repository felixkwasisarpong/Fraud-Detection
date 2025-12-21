package com.example.fraud.controller;

import com.example.fraud.dto.TransactionRequest;
import com.example.fraud.dto.TransactionResponse;
import com.example.fraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	private final TransactionService  transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	@PostMapping
	public ResponseEntity<TransactionResponse> receiveTransaction(@Valid @RequestBody TransactionRequest request){
		TransactionResponse response = transactionService.receiveTransaction(request);
		return ResponseEntity.ok(response);
	}
	

}
