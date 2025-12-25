package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.service.DeviceRuleService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class DeviceRule implements FraudRule {
    private final DeviceRuleService deviceRuleService;
    public DeviceRule(DeviceRuleService deviceRuleService){
        this.deviceRuleService = deviceRuleService;
    }

    @Override
    public FraudCheckResult evaluate(TransactionEvent event){

        if(event.getDeviceId() == null){
            return new FraudCheckResult(false,"NO_DEVICE_IP");
        }
        boolean suspicious = deviceRuleService.isDeviceLinkedToMultipleAccounts(
                event.getDeviceId(),
                event.getDeviceId()
        );

        if(suspicious){
            return new FraudCheckResult(true, "DEVICE_LINKED_TO_MULTIPLE_ACCOUNTS");
        }
        return new FraudCheckResult(false,"OK");
    }
}
