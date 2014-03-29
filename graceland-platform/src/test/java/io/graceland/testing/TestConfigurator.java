package io.graceland.testing;

import io.dropwizard.setup.Environment;
import io.graceland.dropwizard.Configurator;
import io.graceland.PlatformConfiguration;

public class TestConfigurator implements Configurator {
    @Override
    public void configure(PlatformConfiguration configuration, Environment environment) {
        // do nothing
    }
}
