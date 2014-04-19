package io.graceland.platform.plugin.loaders;

import io.graceland.platform.application.Application;

/**
 * Used to load plugins into an {@link io.graceland.platform.application.Application}.
 */
public interface PluginLoader {

    /**
     * Load plugins into the {@link io.graceland.platform.application.Application}.
     *
     * @param application The application to load the plugins into.
     */
    void loadInto(Application application);
}
