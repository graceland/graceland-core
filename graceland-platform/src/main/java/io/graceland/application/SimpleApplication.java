package io.graceland.application;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import io.graceland.plugin.Plugin;

/**
 * A simple application implementation that lets the developer define what plugins are loaded using an abstract method
 * {@link #configure()}.
 */
public abstract class SimpleApplication implements Application {
    private final ImmutableList.Builder<Plugin> pluginBuilder = ImmutableList.builder();
    protected ImmutableList<Plugin> plugins = null;

    /**
     * Configure what plugins to load in this method.
     */
    protected abstract void configure();

    /**
     * Adds the plugin to the list of plugins that will be loaded.
     *
     * @param plugin The plugin to load.
     */
    protected void loadPlugin(Plugin plugin) {
        Preconditions.checkNotNull(plugin, "Plugin cannot be null.");
        pluginBuilder.add(plugin);
    }

    @Override
    public ImmutableList<Plugin> getPlugins() {
        if (plugins == null) {
            configure();
            plugins = pluginBuilder.build();
            Preconditions.checkState(!plugins.isEmpty(), "At least one plugin must be loaded.");
        }

        return plugins;
    }
}
