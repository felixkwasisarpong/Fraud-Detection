package com.example.fraud.ml;


import com.example.fraud.event.TransactionEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class FeatureExtractor {
    public float[] extract(TransactionEvent event, int velocity, int deviceAccounts, int pastDecline){
        return new float[]{
                event.getAmount().floatValue(),
                event.getTimestamp().atZone(ZoneId.of("UTC")).getHour(),
                velocity,
                deviceAccounts,
                pastDecline
        };
    }

}
