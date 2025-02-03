package net.laurus.lgtm.tracing;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TracingAspect {

    private final Tracer tracer;

    /**
     * Automatically traces methods annotated with @Trace.
     *
     * @param joinPoint The method being executed
     * @param trace The Trace annotation
     * @return The method’s return value
     * @throws Throwable If the method throws an exception
     */
    @Around("@annotation(trace)")
    public Object traceMethodExecution(ProceedingJoinPoint joinPoint, Trace trace) throws Throwable {
        String spanName = trace.value().isEmpty() ? joinPoint.getSignature().getName() : trace.value();
        Span span = tracer.spanBuilder(spanName)
                .setSpanKind(trace.type())
                .startSpan();

        log.debug("Starting span: {}", spanName);

        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error occurred in traced method: {}", spanName, e);
            throw e; // Ensure exception is properly propagated
        } finally {
            log.debug("Ending span: {}", spanName);
            span.end();
        }
    }
}
