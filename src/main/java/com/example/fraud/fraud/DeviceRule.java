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
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines) {

        if(event.getDeviceId() == null){
            return new FraudCheckResult(false,"NO_DEVICE_IP",0.0f);
        }
        boolean suspicious = deviceRuleService.isDeviceLinkedToMultipleAccounts(
                event.getDeviceId(),
                event.getDeviceId()
        );

        if(suspicious){
            return new FraudCheckResult(true, "DEVICE_LINKED_TO_MULTIPLE_ACCOUNTS",0.0f);
        }
        return new FraudCheckResult(false,"OK",0.0f);
    }
}
