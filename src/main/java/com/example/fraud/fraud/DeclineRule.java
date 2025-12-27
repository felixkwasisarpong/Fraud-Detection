package com.example.fraud.fraud;

import com.example.fraud.event.TransactionEvent;
import com.example.fraud.redis.RedisStateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeclineRule implements FraudRule {

    private final RedisStateStore redis;
    private static final Logger log = LoggerFactory.getLogger(AmountThresholdRule.class);
    public DeclineRule(RedisStateStore redis) {
        this.redis = redis;
    }

    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines) {

        pastDeclines = redis.getPastDeclines(event.getAccountId());
        FraudCheckResult result;

        if (pastDeclines > 2) {
            result = new FraudCheckResult(
                    true,
                    "PAST_DECLINES_EXCEEDED (" + pastDeclines + ")",0.0f
            );
        }else{

        result = new FraudCheckResult(false, "DECLINE_RULE_PASS",0.0f);
    }
        log.info("RuleExecuted: rule={} txnId={} fraud={} reason={} score={}",
                this.getClass().getSimpleName(),
                event.getTransactionId(),
                result.fraud(),
                result.reason(),
                result.score());
        return result;
}}