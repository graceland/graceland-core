package io.graceland.client.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Preconditions;

import retrofit.Profiler;

public class MeteredProfiler implements Profiler<Timer.Context> {

    private final Timer timer;

    MeteredProfiler(MetricRegistry registry, String metricName) {
        Preconditions.checkNotNull(registry, "Metric Registry cannot be null.");
        Preconditions.checkNotNull(metricName, "Metric Name cannot be null.");
        Preconditions.checkArgument(!metricName.trim().isEmpty(), "Metric Name cannot be empty");

        timer = registry.timer(metricName);
    }

    public static MeteredProfiler newInstance(MetricRegistry registry, String metricName) {
        return new MeteredProfiler(registry, metricName);
    }

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
