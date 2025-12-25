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
    public FraudCheckResult evaluate(TransactionEvent event){
        try{
            int velocity = redis.getVelocity(event.getAccountId());
            int deviceAccounts = redis.getDeviceAccountCount(event.getAccountId());
            int pastDeclines = redis.getPastDeclines(event.getAccountId());

            float[] features = extractor.extract(
                    event,
                    velocity,
                    deviceAccounts,
                    pastDeclines
            );
            float prob = scorer.score(features);

            if (prob >= 0.65) {
                return new FraudCheckResult(true, "ML_MODEL_SCORE=" + prob);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new FraudCheckResult(false, "ML_MODEL_ERROR");
        }

        return new FraudCheckResult(false, "ML_MODEL_CLEAN");
    }

}
