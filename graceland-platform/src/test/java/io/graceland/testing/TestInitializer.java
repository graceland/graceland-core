package io.graceland.testing;

import io.dropwizard.setup.Bootstrap;
import io.graceland.dropwizard.Initializer;
import io.graceland.DefaultPlatformConfiguration;

public class TestInitializer implements Initializer {
    @Override
    public void initialize(Bootstrap<DefaultPlatformConfiguration> bootstrap) {
        // do something
    }
}
