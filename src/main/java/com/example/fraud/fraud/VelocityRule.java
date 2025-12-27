package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.service.VelocityRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class VelocityRule implements FraudRule {
    private final VelocityRuleService velocityRuleService;
    private static final Logger log = LoggerFactory.getLogger(AmountThresholdRule.class);

    public VelocityRule(VelocityRuleService velocityRuleService) {
        this.velocityRuleService = velocityRuleService;
    }


    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines) {
        FraudCheckResult result;
        boolean exceeded = velocityRuleService.isVelocityExceeded(event.getAccountId());
        if (exceeded) {
            result = new FraudCheckResult(
                    true,
                    "VELOCITY_THRESHOLD_EXCEEDED", 0.0f
            );

        } else {
            result = new FraudCheckResult(false, "OK", 0.0f);
        }
        log.info("RuleExecuted: rule={} txnId={} fraud={} reason={} score={}",
                this.getClass().getSimpleName(),
                event.getTransactionId(),
                result.fraud(),
                result.reason(),
                result.score());

        return result;
    }
}
