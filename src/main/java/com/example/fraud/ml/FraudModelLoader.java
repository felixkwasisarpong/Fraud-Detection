package com.example.fraud.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Component
public class FraudModelLoader {

    private final OrtSession session;

    public FraudModelLoader() throws Exception {
        OrtEnvironment env = OrtEnvironment.getEnvironment();

        // Load model from classpath
        ClassPathResource resource =
                new ClassPathResource("models/fraud_logreg_model.onnx");

        // Copy to temp file (ONNX needs a real file path)
        Path tempModel = Files.createTempFile("fraud-model", ".onnx");
        try (InputStream in = resource.getInputStream()) {
            Files.copy(in, tempModel, StandardCopyOption.REPLACE_EXISTING);
        }

        session = env.createSession(
                tempModel.toAbsolutePath().toString(),
                new OrtSession.SessionOptions()
        );
    }

    public OrtSession getSession() {
        return session;
    }
}