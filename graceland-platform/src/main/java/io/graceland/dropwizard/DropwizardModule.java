package io.graceland.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.dropwizard.setup.Environment;
import io.graceland.PlatformConfiguration;
import io.graceland.inject.Graceland;

/**
 * This module is used to provide Dropwizard specific objects to the Guice dependency graph. It will only have access
 * to the {@link io.graceland.PlatformConfiguration} and {@link io.dropwizard.setup.Environment} in the
 * {@link io.graceland.Platform#run(io.graceland.PlatformConfiguration, io.dropwizard.setup.Environment)} method.
 * <p/>
 * This module also provides access to environment objects, such as the
 * {@link com.fasterxml.jackson.databind.ObjectMapper} and the {@link com.codahale.metrics.MetricRegistry}.
 */
public class DropwizardModule extends AbstractModule {

    private PlatformConfiguration configuration = null;
    private Environment environment = null;

    @Override
    protected void configure() {
        // use provides methods
    }

    /**
     * Called when the configuration and environment come into scope.
     *
     * @param config Provided by Dropwizard.
     * @param env    Provided by Dropwizard.
     */
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
