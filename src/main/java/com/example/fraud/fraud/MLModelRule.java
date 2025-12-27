package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.ml.FeatureExtractor;
import com.example.fraud.redis.RedisStateStore;
import com.example.fraud.service.FraudScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MLModelRule implements FraudRule{
    private final FeatureExtractor extractor;
    private final FraudScoringService scorer;
    private final RedisStateStore redis;
    private static final Logger log = LoggerFactory.getLogger(AmountThresholdRule.class);
    public MLModelRule(
            FeatureExtractor extractor,
            FraudScoringService scorer,
            RedisStateStore redis
    ){
        this.extractor = extractor;
        this.scorer = scorer;
        this.redis = redis;
    }

    @Override
    public FraudCheckResult evaluate(TransactionEvent event,
                                     int velocity,
                                     int deviceAccounts,
                                     int pastDeclines) {
        FraudCheckResult result;
        try {
            velocity = redis.getVelocity(event.getAccountId());
            deviceAccounts = redis.getDeviceAccountCount(event.getAccountId());
            pastDeclines = redis.getPastDeclines(event.getAccountId());

            float[] features = extractor.extract(
                    event,
                    velocity,
                    deviceAccounts,
                    pastDeclines
            );
            float prob = scorer.score(features);

            if (prob >= 0.65f) {
                result = FraudCheckResult.fail("ML_MODEL_FRAUD", prob);
            }else {
                // ML says it's safe, but still return score for downstream services
                result = FraudCheckResult.pass(prob);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = FraudCheckResult.error("ML_MODEL_ERROR");
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
