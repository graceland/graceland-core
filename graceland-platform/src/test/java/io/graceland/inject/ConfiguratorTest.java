package io.graceland.inject;

import org.junit.Test;

import io.dropwizard.setup.Environment;
import io.graceland.platforms.PlatformConfiguration;

import static org.mockito.Mockito.mock;

public class ConfiguratorTest {

    private Configurator configurator = mock(Configurator.class);
    private PlatformConfiguration configuration = mock(PlatformConfiguration.class);
    private Environment environment = mock(Environment.class);

    @Test
    public void configure_requires_config_and_environment() {
        configurator.configure(configuration, environment);
    }
}
