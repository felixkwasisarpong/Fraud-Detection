package com.example.fraud.service;

import com.example.fraud.event.TransactionEvent;
import com.example.fraud.fraud.FraudCheckResult;

public interface FraudEvaluationService {
    FraudCheckResult evaluate(TransactionEvent event);
}
