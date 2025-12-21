package com.example.fraud.service.impl;

import com.example.fraud.repository.TransactionRepository;
import com.example.fraud.service.TransactionService;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.example.fraud.dto.TransactionRequest;
import com.example.fraud.dto.TransactionResponse;
import com.example.fraud.entity.Transaction;




@Service
public class TransactionServiceImpl implements TransactionService{
	
	private final TransactionRepository repository;
	public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }
	@Override
	public TransactionResponse receiveTransaction(TransactionRequest request) {
		Transaction txn = new Transaction();
		String txnId = request.getTransactionId() != null
				? request.getTransactionId()
				: UUID.randomUUID().toString();
		txn.setTransactionId(txnId);
		txn.setAccountId(request.getAccountId());
		txn.setMerchantId(request.getMerchantId());
		txn.setAmount(request.getAmount());
		txn.setCurrency(request.getCurrency());
		txn.setTimestamp(Instant.now());
		txn.setStatus(Transaction.Status.RRECEIVED);
		
		repository.save(txn);
		return new TransactionResponse(txn.getTransactionId(),txn.getStatus().name());
	}
}
