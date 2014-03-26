package io.graceland.inject;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.TypeLiteral;

import io.dropwizard.Bundle;
import io.dropwizard.cli.Command;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;

/**
 * A utility class that defines helpful classes and static variables. They are used when interacting with Guice's
 * {@link com.google.inject.Injector}.
 */
public final class TypeLiterals {

    public static TypeLiteral<Set<Object>> ObjectSet = new TypeLiteral<Set<Object>>() {
    };
    // ==========================
    // Health Check Type Literals
    // ==========================
    public static TypeLiteral<Class<? extends HealthCheck>> HealthCheckClass = new TypeLiteral<Class<? extends HealthCheck>>() {
    };
    public static TypeLiteral<Set<HealthCheck>> HealthCheckSet = new TypeLiteral<Set<HealthCheck>>() {
    };
    public static TypeLiteral<Set<Class<? extends HealthCheck>>> HealthCheckClassSet = new TypeLiteral<Set<Class<? extends HealthCheck>>>() {
    };
    // =====================
    // Managed Type Literals
    // =====================
    public static TypeLiteral<Class<? extends Managed>> ManagedClass = new TypeLiteral<Class<? extends Managed>>() {
    };
    public static TypeLiteral<Set<Managed>> ManagedSet = new TypeLiteral<Set<Managed>>() {
    };
    public static TypeLiteral<Set<Class<? extends Managed>>> ManagedClassSet = new TypeLiteral<Set<Class<? extends Managed>>>() {
    };
    // ==================
    // Task Type Literals
    // ==================
    public static TypeLiteral<Class<? extends Task>> TaskClass = new TypeLiteral<Class<? extends Task>>() {
    };
    public static TypeLiteral<Set<Task>> TaskSet = new TypeLiteral<Set<Task>>() {
    };
    public static TypeLiteral<Set<Class<? extends Task>>> TaskClassSet = new TypeLiteral<Set<Class<? extends Task>>>() {
    };
    // ====================
    // Bundle Type Literals
    // ====================
    public static TypeLiteral<Class<? extends Bundle>> BundleClass = new TypeLiteral<Class<? extends Bundle>>() {
    };
    public static TypeLiteral<Set<Bundle>> BundleSet = new TypeLiteral<Set<Bundle>>() {
    };
    public static TypeLiteral<Set<Class<? extends Bundle>>> BundleClassSet = new TypeLiteral<Set<Class<? extends Bundle>>>() {
    };
    // =====================
    // Command Type Literals
    // =====================
    public static TypeLiteral<Class<? extends Command>> CommandClass = new TypeLiteral<Class<? extends Command>>() {
    };
    public static TypeLiteral<Set<Command>> CommandSet = new TypeLiteral<Set<Command>>() {
    };
    public static TypeLiteral<Set<Class<? extends Command>>> CommandClassSet = new TypeLiteral<Set<Class<? extends Command>>>() {
    };
    // =========================
    // Initializer Type Literals
    // =========================
    public static TypeLiteral<Class<? extends Initializer>> InitializerClass = new TypeLiteral<Class<? extends Initializer>>() {
    };
    public static TypeLiteral<Set<Initializer>> InitializerSet = new TypeLiteral<Set<Initializer>>() {
    };
    public static TypeLiteral<Set<Class<? extends Initializer>>> InitializerClassSet = new TypeLiteral<Set<Class<? extends Initializer>>>() {
    };
    // ==========================
    // Configurator Type Literals
    // ==========================
    public static TypeLiteral<Class<? extends Configurator>> ConfiguratorClass = new TypeLiteral<Class<? extends Configurator>>() {
    };
    public static TypeLiteral<Set<Configurator>> ConfiguratorSet = new TypeLiteral<Set<Configurator>>() {
    };
    public static TypeLiteral<Set<Class<? extends Configurator>>> ConfiguratorClassSet = new TypeLiteral<Set<Class<? extends Configurator>>>() {
    };
    protected TypeLiterals() {
        // utility class
    }
}
