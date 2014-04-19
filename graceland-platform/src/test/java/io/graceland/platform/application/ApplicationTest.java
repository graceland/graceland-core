package io.graceland.platform.application;

import java.util.List;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import io.graceland.platform.plugin.Plugin;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public abstract class ApplicationTest {

    protected Application application = newApplication();

    protected abstract Application newApplication();

    @Test
    public void getPlugins_stay_the_same() {
        application.loadPlugin(mock(Plugin.class));

        ImmutableList<Plugin> plugins1 = application.getPlugins();
        ImmutableList<Plugin> plugins2 = application.getPlugins();

        assertThat(plugins1, equalTo(plugins2));
    }

    @Test(expected = IllegalStateException.class)
    public void must_load_at_least_one_plugin() {
        application.getPlugins();
    }

    @Test(expected = NullPointerException.class)
    public void loadPlugin_cannot_take_null() {
        application.loadPlugin(null);
    }

    @Test
    public void can_load_multiple_plugins() {
        final Plugin plugin1 = mock(Plugin.class);
        final Plugin plugin2 = mock(Plugin.class);

        application.loadPlugin(plugin1);
        application.loadPlugin(plugin2);

        List<Plugin> plugins = application.getPlugins();

        assertThat(plugins, contains(plugin1, plugin2));
    }
}
