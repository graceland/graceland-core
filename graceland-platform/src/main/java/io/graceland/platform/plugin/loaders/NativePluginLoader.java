package io.graceland.platform.plugin.loaders;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;

import io.graceland.platform.application.Application;
import io.graceland.platform.plugin.Plugin;

/**
 * Uses the java {@link java.util.ServiceLoader} to discover plugins and loads them into the
 * {@link io.graceland.platform.application.Application}.
 */
public class NativePluginLoader implements PluginLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(NativePluginLoader.class);

    NativePluginLoader() {
        // do nothing
    }

    public static NativePluginLoader newLoader() {
        return new NativePluginLoader();
    }

    @Override
    public void loadInto(Application application) {
        Preconditions.checkNotNull(application, "Application cannot be null.");

        LOGGER.info("Loading plugins using the native ServiceLoader.");

        for (Plugin plugin : ServiceLoader.load(Plugin.class)) {
            LOGGER.debug("Loading Plugin: {}", plugin.getClass().getCanonicalName());
            application.loadPlugin(plugin);
        }
    }
}
