package io.graceland.plugin.loaders;

import org.junit.Test;

import io.graceland.application.Application;

import static org.mockito.Mockito.mock;

public abstract class PluginLoaderTest<T extends PluginLoader> {

    protected T pluginLoader = newPluginLoader();
    protected Application application = mock(Application.class);

    protected abstract T newPluginLoader();

    @Test
    public void can_load_plugins_to_an_application() {
        Application application = mock(Application.class);
        pluginLoader.loadInto(application);
    }

    @Test(expected = NullPointerException.class)
    public void cannot_load_into_a_null_application() {
        pluginLoader.loadInto(null);
    }
}
