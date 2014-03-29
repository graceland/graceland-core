package io.graceland.testing;

import io.dropwizard.setup.Bootstrap;
import io.graceland.PlatformConfiguration;
import io.graceland.dropwizard.Initializer;

public class TestInitializer implements Initializer {
    @Override
    public void initialize(Bootstrap<PlatformConfiguration> bootstrap) {
        // do something
    }
}
