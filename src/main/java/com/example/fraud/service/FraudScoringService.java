package com.example.fraud.service;



import ai.onnxruntime.*;
import com.example.fraud.ml.FraudModelLoader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FraudScoringService {
    private final FraudModelLoader modelLoader;
    private final OrtEnvironment env;
    public FraudScoringService(FraudModelLoader modelLoader){
        this.modelLoader = modelLoader;
        this.env = OrtEnvironment.getEnvironment();
    }

    public float score(float[] features) throws Exception{
        OrtSession session = modelLoader.getSession();
        float[][] input = new float[][] { features };

        try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, input)) {
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("input", inputTensor);

            try (OrtSession.Result result = session.run(inputs)) {
                OnnxValue probabilities = result.get("probabilities").orElse(null);
                if (probabilities == null) {
                    // fall back to the second output if model naming changes
                    probabilities = result.get(1);
                }
                float[][] output = (float[][]) probabilities.getValue();
                return output[0][output[0].length - 1];
            }
        }
    }
}
