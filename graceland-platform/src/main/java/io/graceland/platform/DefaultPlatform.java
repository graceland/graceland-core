package io.graceland.platform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.graceland.applications.Application;
import io.graceland.inject.Configurator;
import io.graceland.inject.DropwizardModule;
import io.graceland.inject.Initializer;
import io.graceland.inject.InjectorWrapper;

/**
 * The default platform for Graceland.
 */
public class DefaultPlatform
        extends io.dropwizard.Application<DefaultPlatformConfiguration>
        implements Platform {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlatform.class);
    private InjectorWrapper wrapper;
    private DropwizardModule dropwizardModule = new DropwizardModule();

    DefaultPlatform(Application application) {
        Preconditions.checkNotNull(application, "Application cannot be null.");

        List<Module> modules = ImmutableList.<Module>builder()
                .addAll(application.getPlugins())
                .add(dropwizardModule)
                .build();

        Injector injector = Guice.createInjector(modules);
        this.wrapper = InjectorWrapper.wrap(injector);
    }

    @Override
    public void start(String[] args) throws Exception {
        Preconditions.checkNotNull(args, "Arguments cannot be null.");
        run(args);
    }

    /**
     * Ran when the Dropwizard service initializes. This method is responsible for setting up the
     * {@link io.dropwizard.Bundle}s and {@link io.dropwizard.cli.Command}s.
     *
     * @param bootstrap Provided by Dropwizard.
     */
    @Override
    public void initialize(Bootstrap<DefaultPlatformConfiguration> bootstrap) {
        for (Initializer initializer : wrapper.getInitializers()) {
            initializer.initialize(bootstrap);
            LOGGER.debug("Registered Initializer: {}", initializer.getClass().getCanonicalName());
        }

        for (Bundle bundle : wrapper.getBundles()) {
            bootstrap.addBundle(bundle);
            LOGGER.debug("Registered Bundle: {}", bundle.getClass().getCanonicalName());
        }

        for (Command command : wrapper.getCommands()) {
            bootstrap.addCommand(command);
            LOGGER.debug("Registered Command: {}", command.getClass().getCanonicalName());
        }
    }

    /**
     * Ran when the Dropwizard service starts up. This method is responsible for setting up the
     * {@link io.dropwizard.setup.Environment} using the bindings from the loaded
     * {@link io.graceland.plugins.Plugin}s.
     *
     * @param configuration Provided by Dropwizard.
     * @param environment   Provided by Dropwizard.
     * @throws Exception Thrown by Dropwizard.
     */
    @Override
    public void run(DefaultPlatformConfiguration configuration, Environment environment) throws Exception {
        dropwizardModule.setup(configuration, environment);

        for (Configurator configurator : wrapper.getConfigurators()) {
            configurator.configure(configuration, environment);
            LOGGER.debug("Registered Configurator: {}", configurator.getClass().getCanonicalName());
        }

        for (Object jerseyComponent : wrapper.getJerseyComponents()) {
            environment.jersey().register(jerseyComponent);
            LOGGER.debug("Registered Jersey Component: {}", jerseyComponent.getClass().getCanonicalName());
        }

        for (Managed managed : wrapper.getManaged()) {
            environment.lifecycle().manage(managed);
            LOGGER.debug("Registered Managed Object: {}", managed.getClass().getCanonicalName());
        }

        for (HealthCheck healthCheck : wrapper.getHealthChecks()) {
            environment.healthChecks().register(healthCheck.toString(), healthCheck);
            LOGGER.debug("Registered Health Check: {}", healthCheck.getClass().getCanonicalName());
        }

        for (Task task : wrapper.getTasks()) {
            environment.admin().addTask(task);
            LOGGER.debug("Registered Task: {}", task.getClass().getCanonicalName());
        }
    }
}
