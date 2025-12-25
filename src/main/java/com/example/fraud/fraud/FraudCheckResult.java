package com.example.fraud.fraud;

public record FraudCheckResult(boolean fraud, String reason, float score) {

    public static FraudCheckResult pass(){
        return new FraudCheckResult(false, "OK",0.0f);

    }

    public static FraudCheckResult pass(float score){
        return new FraudCheckResult(false, "OK",score);

    }

    public static FraudCheckResult fail(String reason, float score){
        return new FraudCheckResult(true, reason,score);

    }

    public static FraudCheckResult error(String mlModelError) {
        return new FraudCheckResult(false, mlModelError, 0.0f);
    }
}
