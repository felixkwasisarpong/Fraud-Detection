package com.example.fraud.consumer;

import com.example.fraud.entity.FraudResult;
import com.example.fraud.event.FraudResultEvent;
import com.example.fraud.event.KafkaTopics;
import com.example.fraud.event.TransactionEvent;
import com.example.fraud.fraud.FraudCheckResult;
import com.example.fraud.redis.RedisStateStore;
import com.example.fraud.repository.FraudResultRepository;
import com.example.fraud.service.FraudEvaluationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.time.Instant;


@Component
public class TransactionListener {

    private final FraudEvaluationService fraudService;
    private final FraudResultRepository fraudResultRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisStateStore redis;
    public TransactionListener(FraudEvaluationService fraudService,
                               FraudResultRepository fraudResultRepository,
                               KafkaTemplate<String, Object> kafkaTemplate,
                               RedisStateStore redis
    ) {
        this.fraudService = fraudService;
        this.fraudResultRepository = fraudResultRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.redis = redis;
    }


    @KafkaListener(topics = KafkaTopics.TRANSACTION_TOPIC, groupId = "fraud-engine-app-group")
    public void processTransaction(TransactionEvent event){
        //update redis state
        int velocity = redis.incrementVelocity(event.getAccountId());
        int deviceAccounts = redis.addDeviceAccount(event.getDeviceId(), event.getAccountId());
        int pastDeclines = redis.getPastDeclines(event.getAccountId());

        //call rule engine
        FraudCheckResult check = fraudService.evaluate(event,velocity,deviceAccounts,pastDeclines);

        //persist to db
        FraudResult result = new FraudResult();
        result.setTransactionId(event.getTransactionId());
        result.setEvaluatedAt(Instant.now());
        if(check.fraud()) {
            result.setResult(FraudResult.Result.FRAUD);
            result.setReason(check.reason());
        }
        else{
            result.setResult(FraudResult.Result.APPROVED);
            result.setReason("OK");
        }
        fraudResultRepository.save(result);

        //publish result to kafka for SNS to SQS to Lambda alerts
        FraudResultEvent out = new FraudResultEvent();
        out.setTransactionId(event.getTransactionId());
        out.setDecision(result.getResult().name());
        out.setReason(result.getReason());
        out.setScore(check.score());
        out.setEvaluatedAt(result.getEvaluatedAt());



        kafkaTemplate.send(KafkaTopics.FRAUD_RESULTS_TOPIC, event.getTransactionId(), out);

        System.out.println("Fraud Evaluation Complete: " + result.getResult());


    }

}
