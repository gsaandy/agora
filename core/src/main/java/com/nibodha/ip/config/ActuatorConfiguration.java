package com.nibodha.ip.config;

import org.springframework.boot.actuate.autoconfigure.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HealthIndicatorAutoConfiguration.class, PublicMetricsAutoConfiguration.class,
        ManagementServerPropertiesAutoConfiguration.class, AuditAutoConfiguration.class,
        EndpointAutoConfiguration.class, TraceRepositoryAutoConfiguration.class,
        MetricRepositoryAutoConfiguration.class})
public class ActuatorConfiguration {
}
