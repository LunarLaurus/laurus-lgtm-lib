package net.laurus.lgtm.metrics;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

public interface Measurable {

	/**
	 * Just @Autowired MeterRegistry meterRegister; and add @Getter.
	 */
    public MeterRegistry getMeterRegistry();

    /**
     * Registers a timer for measuring execution time.
     *
     * @param metricName The name of the metric
     * @return The registered Timer
     */
    default Timer registerTimer(String metricName) {
        return Timer.builder(metricName)
                .description("Auto-measured execution time")
                .register(getMeterRegistry());
    }

    /**
     * Increments a counter for tracking the number of occurrences.
     *
     * @param metricName The name of the metric
     */
    default void count(String metricName) {
        Counter.builder(metricName)
                .description("Counts the number of occurrences")
                .register(getMeterRegistry())
                .increment();
    }

    /**
     * Measures the execution time of a function and registers it in Micrometer.
     *
     * @param metricName The name of the metric
     * @param operation  The function to execute
     * @param <T>        The return type of the function
     * @return The result of the function execution
     */
    default <T> T measure(String metricName, Supplier<T> operation) {
        Timer timer = registerTimer(metricName);
        long start = System.nanoTime();
        try {
            return operation.get();
        } finally {
            timer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Measures execution time for void functions.
     *
     * @param metricName The name of the metric
     * @param operation  The function to execute
     */
    default void measure(String metricName, Runnable operation) {
        Timer timer = registerTimer(metricName);
        long start = System.nanoTime();
        try {
            operation.run();
        } finally {
            timer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }
}
