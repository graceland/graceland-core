package io.graceland.plugin.loaders;

import io.graceland.application.Application;

/**
 * Used to load plugins into an {@link io.graceland.application.Application}.
 */
public interface PluginLoader {

    /**
     * Load plugins into the {@link io.graceland.application.Application}.
     *
     * @param application The application to load the plugins into.
     */
    void loadInto(Application application);
}
