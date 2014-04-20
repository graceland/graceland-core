package io.graceland.platform.testing;

import io.dropwizard.setup.Environment;
import io.graceland.platform.PlatformConfiguration;
import io.graceland.platform.dropwizard.Configurator;

public class TestConfigurator implements Configurator {
    @Override
    public void configure(PlatformConfiguration configuration, Environment environment) {
        // do nothing
    }
}
