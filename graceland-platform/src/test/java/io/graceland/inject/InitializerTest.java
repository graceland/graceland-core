package io.graceland.inject;

import org.junit.Test;

import io.dropwizard.setup.Bootstrap;
import io.graceland.platforms.DefaultPlatformConfiguration;

import static org.mockito.Mockito.mock;

public class InitializerTest {

    private Initializer initializer = mock(Initializer.class);

    @Test
    public void initialize_requires_a_bootstrap() {
        Bootstrap<DefaultPlatformConfiguration> bootstrap = mock(Bootstrap.class);
        initializer.initialize(bootstrap);
    }
}
