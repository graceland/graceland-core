package io.graceland.client.metrics;

import org.junit.Test;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import retrofit.Profiler;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MeteredProfilerTest {

    private MetricRegistry registry = mock(MetricRegistry.class);
    private Timer timer = mock(Timer.class);
    private Timer.Context context = mock(Timer.Context.class);
    private Profiler.RequestInformation requestInfo = new Profiler.RequestInformation("", "", "", 1L, "");

    @Test
    public void beforeCall_returns_a_timer_context() {
        when(registry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(context);

        MeteredProfiler profiler = MeteredProfiler.newInstance(registry);

        Timer.Context actualContext = profiler.beforeCall();

        assertThat(actualContext, is(context));
    }

    @Test
    public void afterCall_stops_the_timer() {
        when(registry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(context);

        MeteredProfiler profiler = MeteredProfiler.newInstance(registry);

        profiler.afterCall(requestInfo, 1L, 200, context);

        verify(context).stop();
    }
}