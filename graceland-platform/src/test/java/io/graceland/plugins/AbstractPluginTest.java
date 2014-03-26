package io.graceland.plugins;

import java.util.Set;

import org.junit.Test;
import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.graceland.inject.Keys;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
import io.graceland.testing.TestHealthCheck;
import io.graceland.testing.TestManaged;
import io.graceland.testing.TestResource;
import io.graceland.testing.TestTask;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class AbstractPluginTest {

    @Test
    public void all_binds_work() {
        final Object jerseyComponent = new Object();
        final Class<TestResource> jerseyComponentClass = TestResource.class;
        final Managed managed = mock(Managed.class);
        final Class<TestManaged> managedClass = TestManaged.class;
        final HealthCheck healthCheck = mock(HealthCheck.class);
        final Class<TestHealthCheck> healthCheckClass = TestHealthCheck.class;
        final Task task = mock(Task.class);
        final Class<TestTask> taskClass = TestTask.class;
        final Bundle bundle = mock(Bundle.class);
        final Class<TestBundle> bundleClass = TestBundle.class;
        final Command command = mock(Command.class);
        final Class<TestCommand> commandClass = TestCommand.class;

        Injector injector = Guice.createInjector(new AbstractPlugin() {
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

                bindBundle(bundleClass);
                bindBundle(bundle);
                bindCommand(command);
                bindCommand(commandClass);
            }
        });

        Set<Object> jerseySet = injector.getInstance(Keys.JerseyComponents);

        Set<Managed> managedSet = injector.getInstance(Keys.ManagedObjects);
        Set<Class<? extends Managed>> managedClassSet = injector.getInstance(Keys.ManagedObjectClasses);

        Set<HealthCheck> healthCheckSet = injector.getInstance(Keys.HealthChecks);
        Set<Class<? extends HealthCheck>> healthCheckClassSet = injector.getInstance(Keys.HealthCheckClasses);

        Set<Task> taskSet = injector.getInstance(Keys.Tasks);
        Set<Class<? extends Task>> taskClassSet = injector.getInstance(Keys.TaskClasses);

        Set<Bundle> bundleSet = injector.getInstance(Keys.Bundles);
        Set<Class<? extends Bundle>> bundleClassSet = injector.getInstance(Keys.BundleClasses);

        Set<Command> commandSet = injector.getInstance(Keys.Commands);
        Set<Class<? extends Command>> commandClassSet = injector.getInstance(Keys.CommandClasses);

        assertThat(jerseySet, hasSize(2));
        assertThat(jerseySet, hasItem(jerseyComponent));
        assertThat(jerseySet, hasItem(TestResource.class));

        assertThat(managedSet, hasSize(1));
        assertThat(managedSet, hasItem(managed));
        assertThat(managedClassSet, hasSize(1));
        assertThat(managedClassSet, hasItem(TestManaged.class));

        assertThat(healthCheckSet, hasSize(1));
        assertThat(healthCheckSet, hasItem(healthCheck));
        assertThat(healthCheckClassSet, hasSize(1));
        assertThat(healthCheckClassSet, hasItem(TestHealthCheck.class));

        assertThat(taskSet, hasSize(1));
        assertThat(taskSet, hasItem(task));
        assertThat(taskClassSet, hasSize(1));
        assertThat(taskClassSet, hasItem(TestTask.class));

        assertThat(bundleSet, hasSize(1));
        assertThat(bundleSet, hasItem(bundle));
        assertThat(bundleClassSet, hasSize(1));
        assertThat(bundleClassSet, hasItem(TestBundle.class));

        assertThat(commandSet, hasSize(1));
        assertThat(commandSet, hasItem(command));
        assertThat(commandClassSet, hasSize(1));
        assertThat(commandClassSet, hasItem(TestCommand.class));
    }
}
