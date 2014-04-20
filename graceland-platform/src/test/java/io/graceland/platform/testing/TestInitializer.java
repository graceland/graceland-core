package io.graceland.platform.testing;

import io.dropwizard.setup.Bootstrap;
import io.graceland.platform.PlatformConfiguration;
import io.graceland.platform.dropwizard.Initializer;

public class TestInitializer implements Initializer {
    @Override
    public void initialize(Bootstrap<PlatformConfiguration> bootstrap) {
        // do something
    }
}
