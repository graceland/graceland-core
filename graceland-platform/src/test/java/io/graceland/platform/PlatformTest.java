package io.graceland.platform;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import io.graceland.platform.application.Application;
import io.graceland.platform.plugin.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class PlatformTest {

    protected abstract Platform newPlatform(Application application);

    @Test(expected = NullPointerException.class)
    public void start_must_be_called_with_args() throws Exception {
        Application application = mock(Application.class);
        Platform platform = newPlatform(application);
        platform.start(null);
    }

    @Test
    public void start_with_no_args() throws Exception {
        Application application = mock(Application.class);
        when(application.getPlugins()).thenReturn(ImmutableList.<Plugin>of());
        String[] args = new String[]{};

        Platform platform = newPlatform(application);
        platform.start(args);
    }
}
