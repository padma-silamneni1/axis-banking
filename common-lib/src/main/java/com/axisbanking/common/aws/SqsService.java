package com.axisbanking.common.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    public void sendMessage(String queueUrl, Object message) {
        try {
            String body = objectMapper.writeValueAsString(message);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build();
            sqsClient.sendMessage(request);
            log.info("Sent message to SQS queue: {}", queueUrl);
        } catch (Exception e) {
            log.error("Failed to send SQS message: {}", e.getMessage());
        }
    }
}
