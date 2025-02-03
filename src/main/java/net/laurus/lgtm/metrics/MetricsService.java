package net.laurus.lgtm.metrics;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricsService implements Measurable {

	@Getter
    private final MeterRegistry meterRegistry;

}
