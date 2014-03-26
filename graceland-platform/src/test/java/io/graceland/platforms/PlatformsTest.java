package io.graceland.platforms;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import io.graceland.applications.Application;
import io.graceland.plugins.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlatformsTest {

    private String[] args = new String[]{"server", "config.yml"};

    @Test
    public void coverage() {
        new Platforms();
    }

    @Test
    public void basic_platform_usage() throws Exception {
        Platform platform = mock(Platform.class);
        platform.start(args);
    }

    @Test(expected = NullPointerException.class)
    public void forApplication_needs_valid_application() {
        Platforms.forApplication(null);
    }

    @Test
    public void uses_an_application_for_customization() throws Exception {
        Application application = mock(Application.class);
        when(application.getPlugins()).thenReturn(ImmutableList.<Plugin>of());

        Platform platform = Platforms.forApplication(application);
    }
}
