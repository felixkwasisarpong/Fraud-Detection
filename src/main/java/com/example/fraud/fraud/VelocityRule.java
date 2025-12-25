package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.service.VelocityRuleService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class VelocityRule implements FraudRule{
    private final VelocityRuleService velocityRuleService;

    public VelocityRule(VelocityRuleService velocityRuleService){
        this.velocityRuleService = velocityRuleService;
    }


    @Override
    public FraudCheckResult evaluate(TransactionEvent event){
        boolean exceeded = velocityRuleService.isVelocityExceeded(event.getAccountId());
        if(exceeded){
            return new FraudCheckResult(
                    true,
                    "VELOCITY_THRESHOLD_EXCEEDED"
            );

        }
        return new FraudCheckResult(false, "OK");
    }
}
