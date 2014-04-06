package io.graceland.plugin;

import java.util.Set;

import org.junit.Test;
import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.graceland.filter.FilterSpec;
import io.graceland.inject.Keys;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
import io.graceland.testing.TestConfiguration;
import io.graceland.testing.TestHealthCheck;
import io.graceland.testing.TestManaged;
import io.graceland.testing.TestResource;
import io.graceland.testing.TestTask;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class AbstractPluginTest {

    @Test
    public void jersey_binds_work() {
        final Object jerseyComponent = new Object();
        final Class<TestResource> jerseyComponentClass = TestResource.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindJerseyComponent(jerseyComponent);
                bindJerseyComponent(jerseyComponentClass);
            }
        });

        Set<Object> jerseySet = injector.getInstance(Keys.JerseyComponents);

        assertThat(jerseySet, hasSize(2));
        assertThat(jerseySet, hasItem(jerseyComponent));
        assertThat(jerseySet, hasItem(TestResource.class));
    }

    @Test
    public void managed_binds_work() {
        final Managed managed = mock(Managed.class);
        final Class<TestManaged> managedClass = TestManaged.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindManaged(managed);
                bindManaged(managedClass);
            }
        });

        Set<Managed> managedSet = injector.getInstance(Keys.ManagedObjects);
        Set<Class<? extends Managed>> managedClassSet = injector.getInstance(Keys.ManagedObjectClasses);

        assertThat(managedSet, hasSize(1));
        assertThat(managedSet, hasItem(managed));
        assertThat(managedClassSet, hasSize(1));
        assertThat(managedClassSet, hasItem(TestManaged.class));
    }

    @Test
    public void healthcheck_binds_work() {
        final HealthCheck healthCheck = mock(HealthCheck.class);
        final Class<TestHealthCheck> healthCheckClass = TestHealthCheck.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindHealthCheck(healthCheck);
                bindHealthCheck(healthCheckClass);
            }
        });

        Set<HealthCheck> healthCheckSet = injector.getInstance(Keys.HealthChecks);
        Set<Class<? extends HealthCheck>> healthCheckClassSet = injector.getInstance(Keys.HealthCheckClasses);

        assertThat(healthCheckSet, hasSize(1));
        assertThat(healthCheckSet, hasItem(healthCheck));
        assertThat(healthCheckClassSet, hasSize(1));
        assertThat(healthCheckClassSet, hasItem(TestHealthCheck.class));
    }

    @Test
    public void task_binds_work() {
        final Task task = mock(Task.class);
        final Class<TestTask> taskClass = TestTask.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindTask(task);
                bindTask(taskClass);
            }
        });

        Set<Task> taskSet = injector.getInstance(Keys.Tasks);
        Set<Class<? extends Task>> taskClassSet = injector.getInstance(Keys.TaskClasses);

        assertThat(taskSet, hasSize(1));
        assertThat(taskSet, hasItem(task));
        assertThat(taskClassSet, hasSize(1));
        assertThat(taskClassSet, hasItem(TestTask.class));
    }

    @Test
    public void bundle_binds_work() {
        final Bundle bundle = mock(Bundle.class);
        final Class<TestBundle> bundleClass = TestBundle.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindBundle(bundle);
                bindBundle(bundleClass);
            }
        });

        Set<Bundle> bundleSet = injector.getInstance(Keys.Bundles);
        Set<Class<? extends Bundle>> bundleClassSet = injector.getInstance(Keys.BundleClasses);

        assertThat(bundleSet, hasSize(1));
        assertThat(bundleSet, hasItem(bundle));
        assertThat(bundleClassSet, hasSize(1));
        assertThat(bundleClassSet, hasItem(TestBundle.class));
    }

    @Test
    public void command_binds_work() {
        final Command command = mock(Command.class);
        final Class<TestCommand> commandClass = TestCommand.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindCommand(command);
                bindCommand(commandClass);
            }
        });

        Set<Command> commandSet = injector.getInstance(Keys.Commands);
        Set<Class<? extends Command>> commandClassSet = injector.getInstance(Keys.CommandClasses);

        assertThat(commandSet, hasSize(1));
        assertThat(commandSet, hasItem(command));
        assertThat(commandClassSet, hasSize(1));
        assertThat(commandClassSet, hasItem(TestCommand.class));
    }

    @Test
    public void configuration_binds_work() {
        final TestConfiguration testConfiguration = new TestConfiguration();

        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindConfiguration(TestConfiguration.class).toInstance(testConfiguration);
            }
        });

        TestConfiguration actualConfiguration = injector.getInstance(Key.get(TestConfiguration.class));

        assertThat(actualConfiguration, is(testConfiguration));
    }

    @Test
    public void filter_binds_work() {
        final FilterSpec filterSpec = mock(FilterSpec.class);


        Injector injector = Guice.createInjector(new AbstractPlugin() {
            @Override
            protected void configure() {
                bindFilter(filterSpec);
            }
        });

        Set<FilterSpec> commandSet = injector.getInstance(Keys.FilterSpecs);

        assertThat(commandSet, hasSize(1));
        assertThat(commandSet, hasItem(filterSpec));
    }
}
