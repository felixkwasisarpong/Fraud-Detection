package com.example.fraud.service.impl;

import com.example.fraud.dto.TransactionRequest;
import com.example.fraud.dto.TransactionResponse;
import com.example.fraud.entity.Transaction;
import com.example.fraud.event.KafkaTopics;
import com.example.fraud.event.TransactionEvent;
import com.example.fraud.repository.TransactionRepository;
import com.example.fraud.service.TransactionService;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository repository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

	public TransactionServiceImpl(TransactionRepository repository,
								  KafkaTemplate<String,Object> kafkaTemplate) {
		this.repository = repository;
		this.kafkaTemplate = kafkaTemplate;
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
		txn.setStatus(Transaction.Status.RECEIVED);

		repository.save(txn);

		// build event
		TransactionEvent event = new TransactionEvent();
		event.setTransactionId(txn.getTransactionId());
		event.setAccountId(txn.getAccountId());
		event.setMerchantId(txn.getMerchantId());
		event.setAmount(txn.getAmount());
		event.setCurrency(txn.getCurrency());
		event.setTimestamp(txn.getTimestamp());

		kafkaTemplate.send(KafkaTopics.TRANSACTION_TOPIC, txn.getTransactionId(), event)
				.whenComplete((SendResult<String, Object> result, Throwable ex) -> {
					if (ex != null) {
						log.error("Failed to publish transaction {} to Kafka", txn.getTransactionId(), ex);
					} else if (result != null) {
						log.info("Published transaction {} to Kafka topic {} partition {} offset {}",
								txn.getTransactionId(),
								result.getRecordMetadata().topic(),
								result.getRecordMetadata().partition(),
								result.getRecordMetadata().offset());
					}
				});
				

		return new TransactionResponse(txn.getTransactionId(), txn.getStatus().name());
	}
}
