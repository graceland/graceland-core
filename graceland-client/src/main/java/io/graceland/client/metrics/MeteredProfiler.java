package io.graceland.client.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Preconditions;

import retrofit.Profiler;

/**
 * A {@link retrofit.Profiler} that records the REST client's performance using the Metrics library.
 *
 * @see retrofit.Profiler
 * @see com.codahale.metrics.MetricRegistry
 * @see com.codahale.metrics.Timer
 */
public class MeteredProfiler implements Profiler<Timer.Context> {

    private final Timer timer;

    MeteredProfiler(MetricRegistry registry, String metricName) {
        Preconditions.checkNotNull(registry, "Metric Registry cannot be null.");
        Preconditions.checkNotNull(metricName, "Metric Name cannot be null.");
        Preconditions.checkArgument(!metricName.trim().isEmpty(), "Metric Name cannot be empty");

        timer = registry.timer(metricName);
    }

    /**
     * Return a new instance of the profiler with a specific metric name.
     *
     * @param registry   The Metric Registry to record performance.
     * @param metricName The name of the metric.
     * @return A new instance of the Profiler.
     */
    public static MeteredProfiler newInstance(MetricRegistry registry, String metricName) {
        return new MeteredProfiler(registry, metricName);
    }

    /**
     * Return a new instance of the profiler with the name specific for a class and possibly extra information. It
     * performs a very similar naming convention to {@link com.codahale.metrics.MetricRegistry#name(Class, String...)}.
     *
     * @param registry   The Metric Registry to record performance.
     * @param klass      A class to base the name of the metric.
     * @param extraNames Any extra naming information.
     * @return A new instance of the Profiler.
     */
    public static MeteredProfiler newInstanceFor(
            MetricRegistry registry,
            Class klass,
            String... extraNames) {

        return new MeteredProfiler(registry, MetricRegistry.name(klass, extraNames));
    }

    @Override
    public Timer.Context beforeCall() {
        return timer.time();
    }

    @Override
    public void afterCall(
            RequestInformation requestInfo,
            long elapsedTime,
            int statusCode,
            Timer.Context beforeCallData) {

        beforeCallData.stop();
    }
}
