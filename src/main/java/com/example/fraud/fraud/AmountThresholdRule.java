package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AmountThresholdRule implements FraudRule{
    private static final double LIMIT = 5000.0;

    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines

    ){
        System.out.println(
                "Evaluating fraud rule | txnId=" + event.getTransactionId() +
                        " amount=" + event.getAmount()
        );
        if (event.getAmount() != null && event.getAmount() > LIMIT){
            return new FraudCheckResult(true, "AMOUNT_EXCEEDS_LIMIT",0.0f);
        }
        return new FraudCheckResult(false, "OK",0.0f);

    }

}
