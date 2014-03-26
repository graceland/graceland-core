package io.graceland.inject;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;

public class InjectorWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectorWrapper.class);
    private final Injector injector;

    InjectorWrapper(Injector injector) {
        this.injector = Preconditions.checkNotNull(injector, "Injector cannot be null.");
    }

    public static InjectorWrapper wrap(Injector injector) {
        Preconditions.checkNotNull(injector, "Injector cannot be null.");
        return new InjectorWrapper(injector);
    }

    private <T> Set<T> getInstancesSafely(Key<Set<T>> key) {
        try {
            return injector.getInstance(key);

        } catch (ConfigurationException e) {
            LOGGER.debug("No bindings found for key: {}", key);
            return ImmutableSet.of();
        }
    }

    private <T> ImmutableSet<T> getGeneric(Key<Set<T>> concreteKey, Key<Set<Class<? extends T>>> classKey) {
        Set<T> instances = getInstancesSafely(concreteKey);
        Set<Class<? extends T>> classes = getInstancesSafely(classKey);

        ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        builder.addAll(instances);

        for (Class<? extends T> klass : classes) {
            T managed = injector.getInstance(klass);
            builder.add(managed);
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public ImmutableSet<Object> getJerseyComponents() {
        Set<Object> components = getInstancesSafely(Keys.JerseyComponents);

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
        return getGeneric(Keys.HealthChecks, Keys.HealthCheckClasses);
    }

    public ImmutableSet<Task> getTasks() {
        return getGeneric(Keys.Tasks, Keys.TaskClasses);
    }

    public ImmutableSet<Managed> getManaged() {
        return getGeneric(Keys.ManagedObjects, Keys.ManagedObjectClasses);
    }

    public ImmutableSet<Bundle> getBundles() {
        return getGeneric(Keys.Bundles, Keys.BundleClasses);
    }

    public ImmutableSet<Command> getCommands() {
        return getGeneric(Keys.Commands, Keys.CommandClasses);
    }

    public ImmutableSet<Initializer> getInitializers() {
        return getGeneric(Keys.Initializers, Keys.InitializerClasses);
    }

    public ImmutableSet<Configurator> getConfigurators() {
        return getGeneric(Keys.Configurators, Keys.ConfiguratorClasses);
    }
}
