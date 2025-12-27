package com.example.fraud.service;

import com.example.fraud.fraud.FraudCheckResult;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;


@Service
public class FraudMetricsService {

    private final Counter fraudCounter;
    private final Counter approvedCounter;
    private final DistributionSummary mlScoreDist;


    public FraudMetricsService(MeterRegistry registry){
        this.fraudCounter = Counter.builder("fraud.decisions.fraud")
                .description("Total APPROVED decisions")
                .register(registry);

        this.approvedCounter = Counter.builder("fraud.decisions.approved")
                .description("Total APPROVED decisions")
                .register(registry);

        this.mlScoreDist = DistributionSummary.builder("fraud.ml.score")
                .description("score")
                .register(registry);
    }

    public void recordDecision(FraudCheckResult result){
        mlScoreDist.record(result.score());
        if(result.fraud()) fraudCounter.increment();
        else approvedCounter.increment();
    }
}
