package com.axisbanking.common.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretsManagerService {

    private final SecretsManagerClient secretsManagerClient;

    @Cacheable(value = "secrets", key = "#secretName")
    public String getSecret(String secretName) {
        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
            return response.secretString();
        } catch (Exception e) {
            log.error("Failed to retrieve secret {}: {}", secretName, e.getMessage());
            throw new RuntimeException("Secret retrieval failed", e);
        }
    }
}
