package io.graceland.client.metrics;

import org.junit.Test;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeteredProfilerTest {
    @Test
    public void beforeCall_returns_a_timer_context() {
        MetricRegistry registry = mock(MetricRegistry.class);
        Timer timer = mock(Timer.class);
        Timer.Context context = mock(Timer.Context.class);

        when(registry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(context);

        MeteredProfiler profiler = MeteredProfiler.newInstance(registry);

        Timer.Context actualContext = profiler.beforeCall();

        assertThat(actualContext, is(context));
    }
}
