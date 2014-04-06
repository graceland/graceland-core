package io.graceland.plugin;

import javax.servlet.Filter;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.graceland.configuration.Configuration;
import io.graceland.configuration.ConfigurationBinder;
import io.graceland.dropwizard.Configurator;
import io.graceland.dropwizard.Initializer;
import io.graceland.filter.FilterBinder;
import io.graceland.inject.Graceland;
import io.graceland.inject.TypeLiterals;

/**
 * This abstract class should be used to create most plugins. It contains helpful functions such as
 * {@link #bindJerseyComponent(Object)} and {@link #bindTask(io.dropwizard.servlets.tasks.Task)} that will build out
 * the necessary multi-binders and use the necessary annotations.
 */
public abstract class AbstractPlugin
        extends AbstractModule
        implements Plugin {

    private Multibinder<Object> jerseyBinder = null;

    private Multibinder<Managed> managedBinder = null;
    private Multibinder<Class<? extends Managed>> managedClassBinder = null;

    private Multibinder<HealthCheck> healthCheckBinder = null;
    private Multibinder<Class<? extends HealthCheck>> healthCheckClassBinder = null;

    private Multibinder<Task> taskBinder = null;
    private Multibinder<Class<? extends Task>> taskClassBinder = null;

    private Multibinder<Bundle> bundleBinder = null;
    private Multibinder<Class<? extends Bundle>> bundleClassBinder = null;

    private Multibinder<Command> commandBinder = null;
    private Multibinder<Class<? extends Command>> commandClassBinder = null;

    private Multibinder<Initializer> initializerBinder = null;
    private Multibinder<Class<? extends Initializer>> initializerClassBinder = null;

    private Multibinder<Configurator> configuratorBinder = null;
    private Multibinder<Class<? extends Configurator>> configuratorClassBinder = null;

    private boolean bindersBuilt = false;

    private void buildBinders() {
        if (!bindersBuilt) {
            jerseyBinder = Multibinder.newSetBinder(binder(), Object.class, Graceland.class);
            managedBinder = Multibinder.newSetBinder(binder(), Managed.class, Graceland.class);
            managedClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.ManagedClass, Graceland.class);
            healthCheckBinder = Multibinder.newSetBinder(binder(), HealthCheck.class, Graceland.class);
            healthCheckClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.HealthCheckClass, Graceland.class);
            taskBinder = Multibinder.newSetBinder(binder(), Task.class, Graceland.class);
            taskClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.TaskClass, Graceland.class);
            bundleBinder = Multibinder.newSetBinder(binder(), Bundle.class, Graceland.class);
            bundleClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.BundleClass, Graceland.class);
            commandBinder = Multibinder.newSetBinder(binder(), Command.class, Graceland.class);
            commandClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.CommandClass, Graceland.class);
            initializerBinder = Multibinder.newSetBinder(binder(), Initializer.class, Graceland.class);
            initializerClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.InitializerClass, Graceland.class);
            configuratorBinder = Multibinder.newSetBinder(binder(), Configurator.class, Graceland.class);
            configuratorClassBinder = Multibinder.newSetBinder(binder(), TypeLiterals.ConfiguratorClass, Graceland.class);

            bindersBuilt = true;
        }
    }

    // ========================
    // Helper Binding Functions
    // ========================

    /**
     * Add a resource to the graceland platform.
     * <p/>
     * There are two ways of adding a resource:
     * <ul>
     * <li><strong>Instance</strong> - bind an instance of a resource directly.</li>
     * <li><strong>Class</strong> - bind a class of a resource, and it will be built by the {@link com.google.inject.Injector}.</li>
     * </ul>
     *
     * @param resource The resource to add, either a {@link java.lang.Class} or an instance.
     */
    protected void bindJerseyComponent(Object resource) {
        Preconditions.checkNotNull(resource, "Resource cannot be null.");
        buildBinders();
        jerseyBinder.addBinding().toInstance(resource);
    }

    protected void bindManaged(Managed managed) {
        Preconditions.checkNotNull(managed, "Managed Object cannot be null.");
        buildBinders();
        managedBinder.addBinding().toInstance(managed);
    }

    protected void bindManaged(Class<? extends Managed> managedClass) {
        Preconditions.checkNotNull(managedClass, "Managed Class cannot be null.");
        buildBinders();
        managedClassBinder.addBinding().toInstance(managedClass);
    }

    protected void bindHealthCheck(HealthCheck healthCheck) {
        Preconditions.checkNotNull(healthCheck, "Health Check cannot be null.");
        buildBinders();
        healthCheckBinder.addBinding().toInstance(healthCheck);
    }

    protected void bindHealthCheck(Class<? extends HealthCheck> healthCheckClass) {
        Preconditions.checkNotNull(healthCheckClass, "Health Check Class cannot be null.");
        buildBinders();
        healthCheckClassBinder.addBinding().toInstance(healthCheckClass);
    }

    protected void bindTask(Task task) {
        Preconditions.checkNotNull(task, "Task cannot be null.");
        buildBinders();
        taskBinder.addBinding().toInstance(task);
    }

    protected void bindTask(Class<? extends Task> taskClass) {
        Preconditions.checkNotNull(taskClass, "Task Class cannot be null.");
        buildBinders();
        taskClassBinder.addBinding().toInstance(taskClass);
    }

    protected void bindBundle(Bundle bundle) {
        Preconditions.checkNotNull(bundle, "Bundle cannot be null.");
        buildBinders();
        bundleBinder.addBinding().toInstance(bundle);
    }

    protected void bindBundle(Class<? extends Bundle> bundleClass) {
        Preconditions.checkNotNull(bundleClass, "Bundle Class cannot be null.");
        buildBinders();
        bundleClassBinder.addBinding().toInstance(bundleClass);
    }

    protected void bindCommand(Command command) {
        Preconditions.checkNotNull(command, "Command cannot be null.");
        buildBinders();
        commandBinder.addBinding().toInstance(command);
    }

    protected void bindCommand(Class<? extends Command> commandClass) {
        Preconditions.checkNotNull(commandClass, "Command Class cannot be null.");
        buildBinders();
        commandClassBinder.addBinding().toInstance(commandClass);
    }

    protected void bindInitializer(Initializer initializer) {
        Preconditions.checkNotNull(initializer, "Initializer cannot be null.");
        buildBinders();
        initializerBinder.addBinding().toInstance(initializer);
    }

    protected void bindInitializer(Class<? extends Initializer> initializerClass) {
        Preconditions.checkNotNull(initializerClass, "Initializer Class cannot be null.");
        buildBinders();
        initializerClassBinder.addBinding().toInstance(initializerClass);
    }

    protected void bindConfigurator(Configurator configurator) {
        Preconditions.checkNotNull(configurator, "Configurator cannot be null.");
        buildBinders();
        configuratorBinder.addBinding().toInstance(configurator);
    }

    protected void bindConfigurator(Class<? extends Configurator> configuratorClass) {
        Preconditions.checkNotNull(configuratorClass, "Configurator Class cannot be null.");
        buildBinders();
        configuratorClassBinder.addBinding().toInstance(configuratorClass);
    }

    protected <T extends Configuration> ConfigurationBinder<T> bindConfiguration(Class<T> configurationClass) {
        Preconditions.checkNotNull(configurationClass, "Configuration Class cannot be null.");
        return ConfigurationBinder.forClass(configurationClass, binder());
    }

    protected FilterBinder buildFilter(Filter filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null.");
        buildBinders();
        return FilterBinder.forInstance(binder(), filter);
    }

    protected FilterBinder buildFilter(Class<? extends Filter> filterClass) {
        Preconditions.checkNotNull(filterClass, "Filter Class cannot be null.");
        buildBinders();
        return FilterBinder.forClass(binder(), filterClass);
    }
}
