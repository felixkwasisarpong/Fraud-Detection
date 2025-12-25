package com.example.fraud.fraud;


import com.example.fraud.event.TransactionEvent;
import com.example.fraud.ml.FeatureExtractor;
import com.example.fraud.redis.RedisStateStore;
import com.example.fraud.service.FraudScoringService;
import org.springframework.stereotype.Component;

@Component
public class MLModelRule implements FraudRule{
    private final FeatureExtractor extractor;
    private final FraudScoringService scorer;
    private final RedisStateStore redis;

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
                return FraudCheckResult.fail("ML_MODEL_FRAUD", prob);
            }
            // ML says it's safe, but still return score for downstream services
            return FraudCheckResult.pass(prob);

        } catch (Exception e) {
            e.printStackTrace();
            return FraudCheckResult.error("ML_MODEL_ERROR");
        }
    }
}
