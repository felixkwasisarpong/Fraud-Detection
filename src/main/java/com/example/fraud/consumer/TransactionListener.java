package com.example.fraud.consumer;

import com.example.fraud.event.KafkaTopics;
import com.example.fraud.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);

    @KafkaListener(
            topics = KafkaTopics.TRANSACTION_TOPIC,
            groupId = "fraud-engine-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void processTransaction(TransactionEvent event){
        log.info("Received transaction event: {}", event);
    }

}
