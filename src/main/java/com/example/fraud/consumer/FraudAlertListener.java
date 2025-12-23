package com.example.fraud.consumer;


import com.example.fraud.alert.FraudNotificationService;
import com.example.fraud.event.FraudResultEvent;
import com.example.fraud.event.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FraudAlertListener {


    private final FraudNotificationService notificationService;

    public FraudAlertListener(FraudNotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @KafkaListener(
            topics = KafkaTopics.FRAUD_RESULTS_TOPIC,
            groupId = "fraud-alert-service",
            properties = "spring.json.value.default.type=com.example.fraud.event.FraudResultEvent"
    )

    public void handleFraudResult(FraudResultEvent event){
        if("FRAUD".equals(event.getDecision())){
            notificationService.sendAlert(event);
            System.out.println("Alert sent to SNS for " + event.getTransactionId());
        }
    }
}
