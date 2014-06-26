package io.graceland.client.metrics;

import org.junit.Test;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import retrofit.Profiler;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MeteredProfilerTest {

    private static final Profiler.RequestInformation REQUEST_INFO = new Profiler.RequestInformation("", "", "", 1L, "");
    private static final String NAME = "name";

    private MetricRegistry registry = mock(MetricRegistry.class);
    private Timer timer = mock(Timer.class);
    private Timer.Context context = mock(Timer.Context.class);

    @Test
    public void beforeCall_returns_a_timer_context() {
        when(registry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(context);

        MeteredProfiler profiler = MeteredProfiler.newInstance(registry, NAME);

        Timer.Context actualContext = profiler.beforeCall();

        assertThat(actualContext, is(context));
    }

    @Test
    public void afterCall_stops_the_timer() {
        when(registry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(context);

        MeteredProfiler profiler = MeteredProfiler.newInstance(registry, NAME);

        profiler.afterCall(REQUEST_INFO, 1L, 200, context);

        verify(context).stop();
    }

    @Test
    public void uses_the_passed_in_name() {
        String name = "my-special-name";
        registry = new MetricRegistry();

        MeteredProfiler.newInstance(registry, name);

        assertThat(registry.getTimers().keySet(), contains(name));
    }

    @Test
    public void uses_the_passed_in_class_as_name() {
        registry = new MetricRegistry();

        MeteredProfiler.newInstanceFor(registry, TestClient.class);

        assertThat(registry.getTimers().keySet(), contains(TestClient.class.getName()));
    }

    @Test
    public void uses_the_passed_in_class_as_name_with_extras() {
        registry = new MetricRegistry();

        MeteredProfiler.newInstanceFor(registry, TestClient.class, "a", "b", "c");

        assertThat(
                registry.getTimers().keySet(),
                contains(TestClient.class.getName() + ".a.b.c"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannot_build_with_empty_name() {
        MeteredProfiler.newInstance(registry, "");
    }

    interface TestClient {
    }
}
