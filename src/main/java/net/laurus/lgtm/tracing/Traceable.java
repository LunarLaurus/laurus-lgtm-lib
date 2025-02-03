package net.laurus.lgtm.tracing;

import java.util.function.Supplier;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

public interface Traceable {

	/**
	 * Just @Autowired Tracer tracer; and add @Getter.
	 */
    public Tracer getTracer();

    /**
     * Executes an operation inside a trace span.
     * @param spanName Name of the trace span.
     * @param operation The function to execute within the span.
     * @param <T> The return type of the function.
     * @return The result of the function execution.
     */
    default <T> T trace(String spanName, Supplier<T> operation) {
        Span span = getTracer().spanBuilder(spanName).startSpan();
        try {
            return operation.get();
        } finally {
            span.end();
        }
    }

    /**
     * Executes a void operation inside a trace span.
     * @param spanName Name of the trace span.
     * @param operation The function to execute within the span.
     */
    default void trace(String spanName, Runnable operation) {
        Span span = getTracer().spanBuilder(spanName).startSpan();
        try {
            operation.run();
        } finally {
            span.end();
        }
    }
}
