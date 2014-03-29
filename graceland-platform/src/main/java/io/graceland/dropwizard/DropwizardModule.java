package io.graceland.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.dropwizard.setup.Environment;
import io.graceland.inject.Graceland;
import io.graceland.PlatformConfiguration;

public class DropwizardModule extends AbstractModule {

    private PlatformConfiguration configuration = null;
    private Environment environment = null;

    @Override
    protected void configure() {
        // use provides methods
    }

    public void setup(PlatformConfiguration config, Environment env) {
        this.configuration = Preconditions.checkNotNull(config, "Configuration cannot be null.");
        this.environment = Preconditions.checkNotNull(env, "Environment cannot be null.");
    }

    @Provides
    @Graceland
    PlatformConfiguration providePlatformConfiguration() {
        Preconditions.checkState(configuration != null, "Configuration has not been set.");
        return configuration;
    }

    @Provides
    @Graceland
    ObjectMapper provideObjectMapper() {
        Preconditions.checkState(environment != null, "Environment has not been set.");
        return environment.getObjectMapper();
    }

    @Provides
    @Graceland
    MetricRegistry provideMetricRegistry() {
        Preconditions.checkState(environment != null, "Environment has not been set.");
        return environment.metrics();
    }
}
