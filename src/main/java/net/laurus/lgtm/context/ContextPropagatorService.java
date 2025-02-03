package net.laurus.lgtm.context;

import java.util.concurrent.Callable;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.laurus.lgtm.tracing.TracingService;

@Service
@RequiredArgsConstructor
public class ContextPropagatorService implements ContextPropagator {

    private final TracingService traceService;

    /**
     * Runs a task inside a new OpenTelemetry span.
     *
     * @param spanName The span name.
     * @param task     The task to execute.
     */
    public void runWithSpan(String spanName, Runnable task) {
        runWithSpan(traceService.getTracer(), spanName, task);
    }

    /**
     * Calls a function inside a new OpenTelemetry span.
     *
     * @param spanName The span name.
     * @param task     The function to execute.
     * @param <T>      The return type.
     * @return The result of the function execution.
     * @throws Exception If an exception occurs during execution.
     */
    public <T> T callWithSpan(String spanName, Callable<T> task) throws Exception {
        return callWithSpan(traceService.getTracer(), spanName, task);
    }

    /**
     * Runs an async task inside the current OpenTelemetry trace context.
     *
     * @param spanName The span name for the async task.
     * @param task     The async task.
     */
    @Async
    public void runAsyncWithContext(String spanName, Runnable task) {
        runAsyncWithContext(traceService.getTracer(), spanName, task);
    }
}
