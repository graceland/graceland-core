package io.graceland;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

import org.junit.Before;
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
import io.graceland.filter.FilterPattern;
import io.graceland.plugin.AbstractPlugin;
import io.graceland.plugin.Plugin;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
import io.graceland.testing.TestConfigurator;
import io.graceland.testing.TestFilter;
import io.graceland.testing.TestHealthCheck;
import io.graceland.testing.TestInitializer;
import io.graceland.testing.TestManaged;
import io.graceland.testing.TestResource;
import io.graceland.testing.TestTask;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlatformTest {

    private PlatformConfiguration configuration = mock(PlatformConfiguration.class);
    private Bootstrap<PlatformConfiguration> bootstrap = mock(Bootstrap.class);

    private Environment environment = mock(Environment.class);
    private LifecycleEnvironment lifecycleEnvironment = mock(LifecycleEnvironment.class);
    private JerseyEnvironment jerseyEnvironment = mock(JerseyEnvironment.class);
    private HealthCheckRegistry healthCheckRegistry = mock(HealthCheckRegistry.class);
    private AdminEnvironment adminEnvironment = mock(AdminEnvironment.class);
    private ServletEnvironment servletEnvironment = mock(ServletEnvironment.class);

    @Before
    public void before() {
        when(environment.jersey()).thenReturn(jerseyEnvironment);
        when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
        when(environment.healthChecks()).thenReturn(healthCheckRegistry);
        when(environment.admin()).thenReturn(adminEnvironment);
        when(environment.servlets()).thenReturn(servletEnvironment);
    }

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
    public void run_adds_jersey_components() throws Exception {
        final Object jerseyComponent = new Object();
        final Class<TestResource> jerseyComponentClass = TestResource.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindJerseyComponent(jerseyComponent);
                        bindJerseyComponent(jerseyComponentClass);
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(jerseyEnvironment).register(eq(jerseyComponent));
        verify(jerseyEnvironment).register(isA(TestResource.class));
    }

    @Test
    public void run_adds_managed() throws Exception {
        final Managed managed = mock(Managed.class);
        final Class<TestManaged> managedClass = TestManaged.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindManaged(managed);
                        bindManaged(managedClass);
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(lifecycleEnvironment).manage(eq(managed));
        verify(lifecycleEnvironment).manage(isA(TestManaged.class));
    }

    @Test
    public void run_adds_healthchecks() throws Exception {
        final HealthCheck healthCheck = mock(HealthCheck.class);
        final Class<TestHealthCheck> healthCheckClass = TestHealthCheck.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindHealthCheck(healthCheck);
                        bindHealthCheck(healthCheckClass);
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(healthCheckRegistry).register(anyString(), eq(healthCheck));
        verify(healthCheckRegistry).register(anyString(), isA(TestHealthCheck.class));
    }

    @Test
    public void run_adds_tasks() throws Exception {
        final Task task = mock(Task.class);
        final Class<TestTask> taskClass = TestTask.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindTask(task);
                        bindTask(taskClass);
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(adminEnvironment).addTask(eq(task));
        verify(adminEnvironment).addTask(isA(TestTask.class));
    }

    @Test
    public void run_adds_configurators() throws Exception {
        final Configurator configurator = mock(Configurator.class);
        final Class<TestConfigurator> configuratorClass = TestConfigurator.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindConfigurator(configurator);
                        bindConfigurator(configuratorClass);
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(configurator).configure(configuration, environment);
        // TODO: Figure out how to check for the class generated configurator
    }

    @Test
    public void run_adds_filters() throws Exception {
        final String filterName = "my-filter-name";
        final Filter filter = mock(Filter.class);
        final Class<TestFilter> filterClass = TestFilter.class;
        final TestFilter patternedFilter = new TestFilter();

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        ImmutableList<String> urlPatterns = ImmutableList.of("/*", "/test");

        final FilterPattern filterPattern = FilterPattern.newInstance(dispatcherTypes, true, urlPatterns);

        FilterRegistration.Dynamic filterDynamic = mock(FilterRegistration.Dynamic.class);
        when(servletEnvironment.addFilter(anyString(), any(TestFilter.class))).thenReturn(filterDynamic);

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        buildFilter(filter)
                                .withName(filterName)
                                .withPriority(999)
                                .withPattern(filterPattern)
                                .bind();
                        buildFilter(filterClass).withPriority(0).bind();
                    }
                }
        );

        new Platform(application).run(configuration, environment);

        verify(servletEnvironment).addFilter(eq(filterClass.getSimpleName()), isA(filterClass));
        verify(servletEnvironment).addFilter(eq(filterName), eq(filter));

        verify(filterDynamic).addMappingForUrlPatterns(eq(dispatcherTypes), eq(true), eq("/*"));
        verify(filterDynamic).addMappingForUrlPatterns(eq(dispatcherTypes), eq(true), eq("/test"));
    }

    @Test
    public void initialize_adds_bundles() {
        final Bundle bundle = mock(Bundle.class);
        final Class<TestBundle> bundleClass = TestBundle.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindBundle(bundle);
                        bindBundle(bundleClass);
                    }
                }
        );

        new Platform(application).initialize(bootstrap);

        verify(bootstrap).addBundle(eq(bundle));
        verify(bootstrap).addBundle(isA(TestBundle.class));
    }

    @Test
    public void initialize_adds_commands() {
        final Command command = mock(Command.class);
        final Class<TestCommand> commandClass = TestCommand.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindCommand(command);
                        bindCommand(commandClass);
                    }
                }
        );

        new Platform(application).initialize(bootstrap);

        verify(bootstrap).addCommand(eq(command));
        verify(bootstrap).addCommand(isA(TestCommand.class));
    }

    @Test
    public void initialize_adds_initializers() {
        final Initializer initializer = mock(Initializer.class);
        final Class<TestInitializer> initializerClass = TestInitializer.class;

        Application application = buildApplication(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindInitializer(initializer);
                        bindInitializer(initializerClass);
                    }
                }
        );

        new Platform(application).initialize(bootstrap);

        // TODO: Add a verification for the class-generated Initializer
        verify(initializer).initialize(eq(bootstrap));
    }

    private Application buildApplication(final Plugin plugin) {
        return new SimpleApplication() {
            @Override
            protected void configure() {
                loadPlugin(plugin);
            }
        };
    }
}
