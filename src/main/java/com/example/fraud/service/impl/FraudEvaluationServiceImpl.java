package com.example.fraud.service.impl;

import com.example.fraud.event.TransactionEvent;
import com.example.fraud.fraud.FraudCheckResult;
import com.example.fraud.fraud.FraudRule;
import com.example.fraud.service.FraudEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudEvaluationServiceImpl implements FraudEvaluationService {
    private  final List<FraudRule> rules;

    public FraudEvaluationServiceImpl(List <FraudRule> rules){
        this.rules = rules;

    }
    @Override
    public FraudCheckResult evaluate(TransactionEvent event){

        for(FraudRule rule : rules){
            FraudCheckResult result = rule.evaluate(event);
            if(result.fraud()){
                return result;
            }
        }
        return new FraudCheckResult(false, "APPROVED");
    }

}
