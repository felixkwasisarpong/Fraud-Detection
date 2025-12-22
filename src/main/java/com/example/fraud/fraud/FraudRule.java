package com.example.fraud.fraud;

import com.example.fraud.event.TransactionEvent;

public interface FraudRule {

    FraudCheckResult evaluate(TransactionEvent event);

}
