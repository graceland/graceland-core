package io.graceland.inject;

import io.dropwizard.setup.Bootstrap;
import io.graceland.platform.DefaultPlatformConfiguration;

/**
 * This class is an escape hatch for customizing Dropwizard to your own liking, without having to be forced to use the
 * helper methods in the {@link io.graceland.plugin.AbstractPlugin}.
 * <p/>
 * All initializers are ran during the initialize phase of a Dropwizard service.
 */
public interface Initializer {
    /**
     * The method that will be called during the Dropwizard's {@link io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap)}
     * method.
     *
     * @param bootstrap Provided by Dropwizard.
     */
    void initialize(Bootstrap<DefaultPlatformConfiguration> bootstrap);
}
