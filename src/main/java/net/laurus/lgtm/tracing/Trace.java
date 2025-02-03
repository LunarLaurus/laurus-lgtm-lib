package net.laurus.lgtm.tracing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.opentelemetry.api.trace.SpanKind;

/**
 * Marks a method for automatic tracing via AOP.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
    String value() default "";
    SpanKind type() default SpanKind.SERVER;
}
