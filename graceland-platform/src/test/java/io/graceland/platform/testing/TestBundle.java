package io.graceland.platform.testing;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(Environment environment) {
        // do nothing
    }
}
