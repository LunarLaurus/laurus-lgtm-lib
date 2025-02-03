package net.laurus.lgtm.context;

import java.util.concurrent.Callable;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

/**
 * Ensures OpenTelemetry spans are propagated correctly across threads and async tasks.
 */
public interface ContextPropagator {

    /**
     * Runs a task within the current OpenTelemetry trace context.
     *
     * @param task The Runnable task to execute.
     */
    default void runWithContext(Runnable task) {
        Context context = Context.current();
        try (Scope ignored = context.makeCurrent()) {
            task.run();
        }
    }

    /**
     * Calls a function within the current OpenTelemetry trace context.
     *
     * @param task The Callable function to execute.
     * @param <T> The return type.
     * @return The result of the function execution.
     * @throws Exception If an exception occurs during execution.
     */
    default <T> T callWithContext(Callable<T> task) throws Exception {
        Context context = Context.current();
        try (Scope ignored = context.makeCurrent()) {
            return task.call();
        }
    }

    /**
     * Runs a task inside a new OpenTelemetry span.
     *
     * @param tracer   The OpenTelemetry tracer instance.
     * @param spanName The span name.
     * @param task     The task to execute.
     */
    default void runWithSpan(Tracer tracer, String spanName, Runnable task) {
        Span span = tracer.spanBuilder(spanName).startSpan();
        Context context = Context.current().with(span);
        try (Scope ignored = context.makeCurrent()) {
            task.run();
        } finally {
            span.end();
        }
    }

    /**
     * Calls a function inside a new OpenTelemetry span.
     *
     * @param tracer   The OpenTelemetry tracer instance.
     * @param spanName The span name.
     * @param task     The function to execute.
     * @param <T>      The return type.
     * @return The result of the function execution.
     * @throws Exception If an exception occurs during execution.
     */
    default <T> T callWithSpan(Tracer tracer, String spanName, Callable<T> task) throws Exception {
        Span span = tracer.spanBuilder(spanName).startSpan();
        Context context = Context.current().with(span);
        try (Scope ignored = context.makeCurrent()) {
            return task.call();
        } finally {
            span.end();
        }
    }

    /**
     * Runs an async task inside the current OpenTelemetry trace context.
     *
     * @param tracer   The OpenTelemetry tracer instance.
     * @param spanName The span name for the async task.
     * @param task     The async task.
     */
    default void runAsyncWithContext(Tracer tracer, String spanName, Runnable task) {
        Span span = tracer.spanBuilder(spanName).startSpan();
        Context context = Context.current().with(span);
        new Thread(() -> {
            try (Scope ignored = context.makeCurrent()) {
                task.run();
            } finally {
                span.end();
            }
        }).start();
    }
}
