package io.graceland.testing;

import io.dropwizard.setup.Environment;
import io.graceland.PlatformConfiguration;
import io.graceland.dropwizard.Configurator;

public class TestConfigurator implements Configurator {
    @Override
    public void configure(PlatformConfiguration configuration, Environment environment) {
        // do nothing
    }
}
