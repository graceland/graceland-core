package io.graceland;

import javax.servlet.Filter;

import org.junit.Test;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.collect.ImmutableList;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.graceland.application.Application;
import io.graceland.application.SimpleApplication;
import io.graceland.dropwizard.Configurator;
import io.graceland.dropwizard.Initializer;
import io.graceland.filter.FilterSpec;
import io.graceland.plugin.AbstractPlugin;
import io.graceland.plugin.Plugin;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
import io.graceland.testing.TestConfigurator;
import io.graceland.testing.TestHealthCheck;
import io.graceland.testing.TestInitializer;
import io.graceland.testing.TestManaged;
import io.graceland.testing.TestResource;
import io.graceland.testing.TestTask;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlatformTest {

    protected Platform newPlatform(Application application) {
        return new Platform(application);
    }

    @Test(expected = NullPointerException.class)
    public void start_must_be_called_with_args() throws Exception {
        Application application = mock(Application.class);
        Platform platform = newPlatform(application);
        platform.start(null);
    }

    @Test
    public void can_build_with_application() {
        Application application = mock(Application.class);
        when(application.getPlugins()).thenReturn(ImmutableList.<Plugin>of());

        Platform.forApplication(application);
    }

    @Test(expected = NullPointerException.class)
    public void cannot_build_with_null_application() {
        Platform.forApplication(null);
    }

    @Test(expected = NullPointerException.class)
    public void constructed_with_valid_application() {
        new Platform(null);
    }

    @Test
    public void start_with_no_args() throws Exception {
        Application application = mock(Application.class);
        when(application.getPlugins()).thenReturn(ImmutableList.<Plugin>of());
        String[] args = new String[]{};
        new Platform(application).start(args);
    }

    @Test
    public void run_adds_all_environment_components() throws Exception {
        final Object jerseyComponent = new Object();
        final Class<TestResource> jerseyComponentClass = TestResource.class;
        final Managed managed = mock(Managed.class);
        final Class<TestManaged> managedClass = TestManaged.class;
        final HealthCheck healthCheck = mock(HealthCheck.class);
        final Class<TestHealthCheck> healthCheckClass = TestHealthCheck.class;
        final Task task = mock(Task.class);
        final Class<TestTask> taskClass = TestTask.class;
        final Configurator configurator = mock(Configurator.class);
        final Class<TestConfigurator> configuratorClass = TestConfigurator.class;

        Filter filter = mock(Filter.class);
        String filterName = "my-filter-name";
        final FilterSpec filterSpec = FilterSpec
                .forFilter(filter)
                .withName(filterName)
                .build();

        Application application = new SimpleApplication() {
            @Override
            protected void configure() {
                loadPlugin(new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindJerseyComponent(jerseyComponent);
                        bindJerseyComponent(jerseyComponentClass);
                        bindManaged(managed);
                        bindManaged(managedClass);
                        bindHealthCheck(healthCheck);
                        bindHealthCheck(healthCheckClass);
                        bindTask(task);
                        bindTask(taskClass);
                        bindConfigurator(configurator);
                        bindConfigurator(configuratorClass);
                        bindFilter(filterSpec);
                    }
                });
            }
        };

        PlatformConfiguration configuration = mock(PlatformConfiguration.class);

        Environment environment = mock(Environment.class);
        LifecycleEnvironment lifecycleEnvironment = mock(LifecycleEnvironment.class);
        JerseyEnvironment jerseyEnvironment = mock(JerseyEnvironment.class);
        HealthCheckRegistry healthCheckRegistry = mock(HealthCheckRegistry.class);
        AdminEnvironment adminEnvironment = mock(AdminEnvironment.class);
        ServletEnvironment servletEnvironment = mock(ServletEnvironment.class);

        when(environment.jersey()).thenReturn(jerseyEnvironment);
        when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
        when(environment.healthChecks()).thenReturn(healthCheckRegistry);
        when(environment.admin()).thenReturn(adminEnvironment);
        when(environment.servlets()).thenReturn(servletEnvironment);

        new Platform(application).run(configuration, environment);

        verify(jerseyEnvironment).register(eq(jerseyComponent));
        verify(jerseyEnvironment).register(isA(TestResource.class));

        verify(lifecycleEnvironment).manage(eq(managed));
        verify(lifecycleEnvironment).manage(isA(TestManaged.class));

        verify(healthCheckRegistry).register(anyString(), eq(healthCheck));
        verify(healthCheckRegistry).register(anyString(), isA(TestHealthCheck.class));

        verify(adminEnvironment).addTask(eq(task));
        verify(adminEnvironment).addTask(isA(TestTask.class));

        verify(configurator).configure(configuration, environment);

        verify(servletEnvironment).addFilter(eq(filterName), eq(filter));
    }

    @Test
    public void initialize_adds_all_components() {
        final Bundle bundle = mock(Bundle.class);
        final Class<TestBundle> bundleClass = TestBundle.class;
        final Command command = mock(Command.class);
        final Class<TestCommand> commandClass = TestCommand.class;
        final Initializer initializer = mock(Initializer.class);
        final Class<TestInitializer> initializerClass = TestInitializer.class;

        Application application = new SimpleApplication() {
            @Override
            protected void configure() {
                loadPlugin(new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindBundle(bundle);
                        bindBundle(bundleClass);
                        bindCommand(command);
                        bindCommand(commandClass);
                        bindInitializer(initializer);
                        bindInitializer(initializerClass);
                    }
                });
            }
        };

        Bootstrap<PlatformConfiguration> bootstrap = mock(Bootstrap.class);

        new Platform(application).initialize(bootstrap);

        verify(bootstrap).addBundle(eq(bundle));
        verify(bootstrap).addBundle(isA(TestBundle.class));

        verify(bootstrap).addCommand(eq(command));
        verify(bootstrap).addCommand(isA(TestCommand.class));

        verify(initializer).initialize(eq(bootstrap));
    }
}
