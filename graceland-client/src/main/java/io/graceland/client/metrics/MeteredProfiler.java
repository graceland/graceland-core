package io.graceland.client.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Preconditions;

import retrofit.Profiler;

public class MeteredProfiler implements Profiler<Timer.Context> {

    private final Timer timer;

    MeteredProfiler(MetricRegistry registry) {
        Preconditions.checkNotNull(registry, "Metric Registry cannot be null.");

        timer = registry.timer("timer");
    }

    public static MeteredProfiler newInstance(MetricRegistry registry) {
        return new MeteredProfiler(registry);
    }

    @Override
    public Timer.Context beforeCall() {
        return timer.time();
    }

    @Override
    public void afterCall(RequestInformation requestInfo, long elapsedTime, int statusCode, Timer.Context beforeCallData) {

    }
}
