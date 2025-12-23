package com.example.fraud.alert;


import com.example.fraud.event.FraudResultEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest; // ✅ CORRECT

@Service
public class FraudNotificationService {
    private final SnsClient snsClient;


    // replace with your SNS topic ARN
    @Value("${aws.sns.fraud-alerts-topic-arn}")
    private String topicArn;

    public FraudNotificationService(SnsClient snsClient){
        this.snsClient = snsClient;
    }

    public void sendAlert(FraudResultEvent event) {

        String message = """
            FRAUD DETECTED
            Transaction: %s
            Reason: %s
            Score: %.2f
            """.formatted(
                event.getTransactionId(),
                event.getReason(),
                event.getScore()
        );

        snsClient.publish(
                PublishRequest.builder()
                        .topicArn(topicArn)   //  builder setter
                        .message(message)
                        .build()              // build LAST
        );
    }
}
