package com.example.fraud.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Component;

@Component
public class FraudModelLoader {
    private final OrtSession session;

    public FraudModelLoader() throws Exception{
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        session = env.createSession("src/main/resources/models/fraud_logreg_model.onnx",
                new OrtSession.SessionOptions()
        );
    }

    public OrtSession getSession(){
       return session;
    }
}
