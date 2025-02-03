package net.laurus.lgtm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Data
@Configuration
@ConfigurationProperties(prefix = "otel")
@Getter
@Slf4j
public class LaurusLgtmConfigProperties {
	
	private String name;
    private Exporter exporter = new Exporter();
    private Logs logs = new Logs();
    private Metrics metrics = new Metrics();
    private Traces traces = new Traces();
    
    @PostConstruct
    public void log() {
        log.info("Initializing OpenTelemetry for service: {}", getName());
        log.info("OTLP Endpoint: {}", getExporter().getOtlp().getEndpoint());
        log.info("Prometheus Endpoint: {}", getMetrics().getPrometheus().getEndpoint());
    }

    @Data
    public static class Exporter {
        private Otlp otlp = new Otlp();

        @Data
        public static class Otlp {
            private String endpoint;
            private String protocol;
        }
    }

    @Data
    public static class Logs {
        private String level;
    }

    @Data
    public static class Metrics {
        private boolean enabled;
        private Prometheus prometheus = new Prometheus();

        @Data
        public static class Prometheus {
            private String endpoint;
        }
    }

    @Data
    public static class Traces {
        private boolean enabled;
        private String sampler;
        private String exporter;
    }

    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("net.laurus.lgtm");
    }
}
