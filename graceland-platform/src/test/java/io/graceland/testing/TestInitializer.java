package io.graceland.testing;

import io.dropwizard.setup.Bootstrap;
import io.graceland.inject.Initializer;
import io.graceland.platform.DefaultPlatformConfiguration;

public class TestInitializer implements Initializer {
    @Override
    public void initialize(Bootstrap<DefaultPlatformConfiguration> bootstrap) {
        // do something
    }
}
