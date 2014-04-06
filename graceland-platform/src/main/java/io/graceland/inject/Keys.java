package io.graceland.inject;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Key;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.graceland.dropwizard.Configurator;
import io.graceland.dropwizard.Initializer;
import io.graceland.filter.FilterSpec;

/**
 * A utility class that defines helpful classes and static variables. They are used when interacting with Guice's
 * {@link com.google.inject.Injector}.
 */
public final class Keys {

    protected Keys() {
        // utility class
    }

    // ===================
    // Guice Injector Keys
    // ===================

    // Managed Objects
    public static final Key<Set<Managed>> ManagedObjects = Key.get(TypeLiterals.ManagedSet, Graceland.class);
    public static final Key<Set<Class<? extends Managed>>> ManagedObjectClasses = Key.get(TypeLiterals.ManagedClassSet, Graceland.class);

    // Jersey Components
    public static final Key<Set<Object>> JerseyComponents = Key.get(TypeLiterals.ObjectSet, Graceland.class);

    // Health Checks
    public static final Key<Set<HealthCheck>> HealthChecks = Key.get(TypeLiterals.HealthCheckSet, Graceland.class);
    public static final Key<Set<Class<? extends HealthCheck>>> HealthCheckClasses = Key.get(TypeLiterals.HealthCheckClassSet, Graceland.class);

    // Tasks
    public static final Key<Set<Task>> Tasks = Key.get(TypeLiterals.TaskSet, Graceland.class);
    public static final Key<Set<Class<? extends Task>>> TaskClasses = Key.get(TypeLiterals.TaskClassSet, Graceland.class);

    // Bundles
    public static final Key<Set<Bundle>> Bundles = Key.get(TypeLiterals.BundleSet, Graceland.class);
    public static final Key<Set<Class<? extends Bundle>>> BundleClasses = Key.get(TypeLiterals.BundleClassSet, Graceland.class);

    // Commands
    public static final Key<Set<Command>> Commands = Key.get(TypeLiterals.CommandSet, Graceland.class);
    public static final Key<Set<Class<? extends Command>>> CommandClasses = Key.get(TypeLiterals.CommandClassSet, Graceland.class);

    // Initializers
    public static final Key<Set<Initializer>> Initializers = Key.get(TypeLiterals.InitializerSet, Graceland.class);
    public static final Key<Set<Class<? extends Initializer>>> InitializerClasses = Key.get(TypeLiterals.InitializerClassSet, Graceland.class);

    // Configurators
    public static final Key<Set<Configurator>> Configurators = Key.get(TypeLiterals.ConfiguratorSet, Graceland.class);
    public static final Key<Set<Class<? extends Configurator>>> ConfiguratorClasses = Key.get(TypeLiterals.ConfiguratorClassSet, Graceland.class);

    // Filter Specs
    public static final Key<Set<FilterSpec>> FilterSpecs = Key.get(TypeLiterals.FilterSpecSet, Graceland.class);
}
