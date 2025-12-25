package com.example.fraud.fraud;

import com.example.fraud.event.TransactionEvent;
import com.example.fraud.redis.RedisStateStore;
import org.springframework.stereotype.Component;

@Component
public class DeclineRule implements FraudRule {

    private final RedisStateStore redis;

    public DeclineRule(RedisStateStore redis) {
        this.redis = redis;
    }

    @Override
    public FraudCheckResult evaluate(TransactionEvent event) {

        int pastDeclines = redis.getPastDeclines(event.getAccountId());

        if (pastDeclines > 2) {
            return new FraudCheckResult(
                    true,
                    "PAST_DECLINES_EXCEEDED (" + pastDeclines + ")"
            );
        }

        return new FraudCheckResult(false, "DECLINE_RULE_PASS");
    }
}