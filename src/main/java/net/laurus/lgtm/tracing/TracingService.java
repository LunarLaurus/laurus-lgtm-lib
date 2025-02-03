package net.laurus.lgtm.tracing;

import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Tracer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TracingService implements Traceable {

	@Getter
	private final Tracer tracer;
	
}
