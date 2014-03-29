package io.graceland.testing;

import io.dropwizard.setup.Environment;
import io.graceland.inject.Configurator;
import io.graceland.platform.PlatformConfiguration;

public class TestConfigurator implements Configurator {
    @Override
    public void configure(PlatformConfiguration configuration, Environment environment) {
        // do nothing
    }
}
