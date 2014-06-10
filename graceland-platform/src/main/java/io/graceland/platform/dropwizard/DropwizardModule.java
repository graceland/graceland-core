package io.graceland.platform.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.dropwizard.setup.Environment;
import io.graceland.platform.PlatformConfiguration;
import io.graceland.platform.inject.Graceland;

/**
 * This module is used to provide Dropwizard specific objects to the Guice dependency graph. It will only have access
 * to the {@link io.graceland.platform.PlatformConfiguration} and {@link io.dropwizard.setup.Environment} in the
 * {@link io.graceland.platform.DropwizardPlatform#run(io.graceland.platform.PlatformConfiguration, io.dropwizard.setup.Environment)}
 * method.
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
    @Singleton
    @Graceland
    PlatformConfiguration providePlatformConfiguration() {
        Preconditions.checkState(configuration != null, "Configuration has not been set.");
        return configuration;
    }

    @Provides
    @Singleton
    @Graceland
    Environment provideEnvironment() {
        Preconditions.checkState(environment != null, "Environment has not been set.");
        return environment;
    }

    @Provides
    @Singleton
    @Graceland
    ObjectMapper provideObjectMapper() {
        Preconditions.checkState(environment != null, "Environment has not been set.");
        return environment.getObjectMapper();
    }

    @Provides
    @Singleton
    @Graceland
    MetricRegistry provideMetricRegistry() {
        Preconditions.checkState(environment != null, "Environment has not been set.");
        return environment.metrics();
    }
}
