package io.graceland.inject;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.graceland.dropwizard.Configurator;
import io.graceland.dropwizard.Initializer;
import io.graceland.filter.FilterSpec;

/**
 * Wraps a Guice {@link com.google.inject.Injector} and provides helper functions to help deal with bindings coming
 * from a {@link com.google.inject.multibindings.Multibinder}.
 */
public class InjectorWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectorWrapper.class);
    private final Injector injector;

    InjectorWrapper(Injector injector) {
        this.injector = Preconditions.checkNotNull(injector, "Injector cannot be null.");
    }

    /**
     * Returns a wrapper around the {@link com.google.inject.Injector}.
     *
     * @param injector The injector to wrap.
     * @return A wrapper.
     */
    public static InjectorWrapper wrap(Injector injector) {
        Preconditions.checkNotNull(injector, "Injector cannot be null.");
        return new InjectorWrapper(injector);
    }

    // =====================
    // KISS Helper Functions
    // =====================

    private <T> Set<T> getSetSafely(Key<Set<T>> key) {
        try {
            return injector.getInstance(key);

        } catch (ConfigurationException e) {
            LOGGER.debug("No bindings found for key: {}", key, e);
            return ImmutableSet.of();
        }
    }

    private <T> ImmutableSet<T> getAndBuildInstances(Key<Set<T>> concreteKey, Key<Set<Class<? extends T>>> classKey) {
        Set<T> instances = getSetSafely(concreteKey);
        Set<Class<? extends T>> classes = getSetSafely(classKey);

        ImmutableSet.Builder<T> allInstances = ImmutableSet.builder();
        allInstances.addAll(instances);

        for (Class<? extends T> klass : classes) {
            T instance = injector.getInstance(klass);
            allInstances.add(instance);
        }

        return allInstances.build();
    }

    // =============================
    // Injector Multibinder Wrappers
    // =============================

    /**
     * Returns a Set of Jersey components, including Providers and Resources. These objects are usually fed into the
     * {@link io.dropwizard.jersey.setup.JerseyEnvironment#register(Class)} method.
     *
     * @return An immutable set of Jersey components.
     */
    @SuppressWarnings("unchecked")
    public ImmutableSet<Object> getJerseyComponents() {
        Set<Object> components = getSetSafely(Keys.JerseyComponents);

        ImmutableSet.Builder<Object> builder = ImmutableSet.builder();

        for (Object classOrInstance : components) {
            if (classOrInstance instanceof Class) {
                Object instance = injector.getInstance((Class<Object>) classOrInstance);
                builder.add(instance);

            } else {
                builder.add(classOrInstance);
            }
        }

        return builder.build();
    }

    public ImmutableSet<HealthCheck> getHealthChecks() {
        return getAndBuildInstances(Keys.HealthChecks, Keys.HealthCheckClasses);
    }

    public ImmutableSet<Task> getTasks() {
        return getAndBuildInstances(Keys.Tasks, Keys.TaskClasses);
    }

    public ImmutableSet<Managed> getManaged() {
        return getAndBuildInstances(Keys.ManagedObjects, Keys.ManagedObjectClasses);
    }

    public ImmutableSet<Bundle> getBundles() {
        return getAndBuildInstances(Keys.Bundles, Keys.BundleClasses);
    }

    public ImmutableSet<Command> getCommands() {
        return getAndBuildInstances(Keys.Commands, Keys.CommandClasses);
    }

    public ImmutableSet<Initializer> getInitializers() {
        return getAndBuildInstances(Keys.Initializers, Keys.InitializerClasses);
    }

    public ImmutableSet<Configurator> getConfigurators() {
        return getAndBuildInstances(Keys.Configurators, Keys.ConfiguratorClasses);
    }

    public ImmutableList<FilterSpec> getFilterSpecs() {
        return FluentIterable
                .from(getSetSafely(Keys.FilterSpecs))
                .toSortedList(FilterSpec.PRIORITY_COMPARATOR);
    }
}
