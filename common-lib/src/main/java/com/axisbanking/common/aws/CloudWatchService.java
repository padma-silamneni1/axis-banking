package com.axisbanking.common.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudWatchService {

    private final CloudWatchClient cloudWatchClient;

    @Value("${cloud.aws.cloudwatch.namespace:AxisBanking}")
    private String namespace;

    public void publishMetric(String metricName, double value, String unit) {
        try {
            MetricDatum datum = MetricDatum.builder()
                    .metricName(metricName)
                    .value(value)
                    .unit(StandardUnit.fromValue(unit))
                    .timestamp(Instant.now())
                    .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                    .namespace(namespace)
                    .metricData(datum)
                    .build();

            cloudWatchClient.putMetricData(request);
            log.debug("Published metric {} = {} to CloudWatch", metricName, value);
        } catch (Exception e) {
            log.warn("Failed to publish CloudWatch metric: {}", e.getMessage());
        }
    }

    public void publishApiLatency(String serviceName, String endpoint, double latencyMs) {
        publishMetric(serviceName + ".api." + endpoint + ".latency", latencyMs, "Milliseconds");
    }

    public void publishErrorCount(String serviceName, String errorType) {
        publishMetric(serviceName + ".errors." + errorType, 1.0, "Count");
    }
}
