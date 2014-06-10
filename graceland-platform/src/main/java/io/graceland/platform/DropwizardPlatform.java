package io.graceland.platform;

import java.util.List;
import javax.servlet.FilterRegistration;

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
import io.graceland.platform.application.Application;
import io.graceland.platform.dropwizard.Configurator;
import io.graceland.platform.dropwizard.DropwizardModule;
import io.graceland.platform.dropwizard.Initializer;
import io.graceland.platform.filter.FilterPattern;
import io.graceland.platform.filter.FilterSpec;
import io.graceland.platform.inject.InjectorWrapper;

/**
 * Uses a Dropwizard Application to provide the {@link io.graceland.platform.Platform} functionality.
 *
 * @see io.graceland.platform.Platform
 * @see io.dropwizard.Application
 */
public class DropwizardPlatform
        extends io.dropwizard.Application<PlatformConfiguration>
        implements Platform {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropwizardPlatform.class);
    private final InjectorWrapper wrapper;
    private final DropwizardModule dropwizardModule = new DropwizardModule();

    DropwizardPlatform(Application application) {
        Preconditions.checkNotNull(application, "Application cannot be null.");

        List<Module> modules = ImmutableList.<Module>builder()
                .addAll(application.getPlugins())
                .add(dropwizardModule)
                .build();

        Injector injector = Guice.createInjector(modules);
        this.wrapper = InjectorWrapper.wrap(injector);
    }

    /**
     * The simplest use case.
     *
     * @param application The application to use.
     * @return A working Platform.
     */
    public static Platform forApplication(Application application) {
        Preconditions.checkNotNull(application, "Application cannot be null.");
        return new DropwizardPlatform(application);
    }

    /**
     * Starts the platform.
     * <p/>
     * It will start up the Dropwizard service, calling {@link io.dropwizard.Application#run(String[])} with the
     * arguments passed in.
     *
     * @param args The command line arguments.
     * @throws Exception Starting up a Dropwizard service may throw an exception.
     */
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
    public void initialize(Bootstrap<PlatformConfiguration> bootstrap) {
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
     * {@link io.graceland.platform.plugin.Plugin}s.
     *
     * @param configuration Provided by Dropwizard.
     * @param environment   Provided by Dropwizard.
     * @throws Exception Thrown by Dropwizard.
     */
    @Override
    public void run(PlatformConfiguration configuration, Environment environment) throws Exception {
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

        for (FilterSpec filterSpec : wrapper.getFilterSpecs()) {
            registerFilterSpec(environment, filterSpec);

            LOGGER.debug("Registered Filter {}: {}",
                    filterSpec.getName(),
                    filterSpec.getFilter().getClass().getCanonicalName());
        }
    }

    private void registerFilterSpec(Environment environment, FilterSpec filterSpec) {
        FilterRegistration.Dynamic dynamic = environment.servlets().addFilter(
                filterSpec.getName(),
                filterSpec.getFilter());

        for (FilterPattern pattern : filterSpec.getPatterns()) {
            for (String urlPattern : pattern.getUrlPatterns()) {
                dynamic.addMappingForUrlPatterns(pattern.getDispatcherTypes(), pattern.isMatchAfter(), urlPattern);
            }
        }
    }
}
