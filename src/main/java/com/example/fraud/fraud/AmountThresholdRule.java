package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AmountThresholdRule implements FraudRule{
    private static final double LIMIT = 5000.0;
    private static final Logger log = LoggerFactory.getLogger(AmountThresholdRule.class);
    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines

    ) {

        FraudCheckResult result;


        if (event.getAmount() != null && event.getAmount() > LIMIT) {
            result = new FraudCheckResult(true, "AMOUNT_EXCEEDS_LIMIT", 0.0f);
        }else{
        result = new FraudCheckResult(false, "OK", 0.0f);

    }
        log.info("RuleExecuted: rule={} txnId={} fraud={} reason={} score={}",
                this.getClass().getSimpleName(),
                event.getTransactionId(),
                result.fraud(),
                result.reason(),
                result.score());
    return  result;
    }

}
