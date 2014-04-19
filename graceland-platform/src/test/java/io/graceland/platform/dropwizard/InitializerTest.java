package io.graceland.platform.dropwizard;

import org.junit.Test;

import io.dropwizard.setup.Bootstrap;
import io.graceland.platform.PlatformConfiguration;

import static org.mockito.Mockito.mock;

public class InitializerTest {

    private Initializer initializer = mock(Initializer.class);

    @Test
    public void initialize_requires_a_bootstrap() {
        Bootstrap<PlatformConfiguration> bootstrap = mock(Bootstrap.class);
        initializer.initialize(bootstrap);
    }
}
