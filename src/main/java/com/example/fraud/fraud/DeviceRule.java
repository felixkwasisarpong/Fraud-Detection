package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.service.DeviceRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class DeviceRule implements FraudRule {
    private final DeviceRuleService deviceRuleService;
    private static final Logger log = LoggerFactory.getLogger(AmountThresholdRule.class);

    public DeviceRule(DeviceRuleService deviceRuleService){
        this.deviceRuleService = deviceRuleService;
    }

    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines) {

        FraudCheckResult result;

        if(event.getDeviceId() == null){
            result = new FraudCheckResult(false,"NO_DEVICE_IP",0.0f);
        }
        boolean suspicious = deviceRuleService.isDeviceLinkedToMultipleAccounts(
                event.getDeviceId(),
                event.getDeviceId()
        );

        if(suspicious){
            result = new FraudCheckResult(true, "DEVICE_LINKED_TO_MULTIPLE_ACCOUNTS",0.0f);
        }else {
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
