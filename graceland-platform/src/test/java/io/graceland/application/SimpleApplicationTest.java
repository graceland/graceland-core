package io.graceland.application;

import java.util.List;

import org.junit.Test;

import io.graceland.plugin.Plugin;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SimpleApplicationTest extends ApplicationTest {

    @Override
    protected Application newApplication() {
        return new SimpleApplication() {
            @Override
            protected void configure() {
                loadPlugin(mock(Plugin.class));
            }
        };
    }

    @Override
    protected Application newEmptyApplication() {
        return new SimpleApplication() {
            @Override
            protected void configure() {
                // do not load a plugin
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void loadPlugin_cannot_take_null() {
        new SimpleApplication() {

            @Override
            protected void configure() {
                loadPlugin(null);
            }
        }.getPlugins();
    }

    @Test
    public void can_load_multiple_plugins() {
        final Plugin plugin1 = mock(Plugin.class);
        final Plugin plugin2 = mock(Plugin.class);

        Application application = new SimpleApplication() {
            @Override
            protected void configure() {
                loadPlugin(plugin1);
                loadPlugin(plugin2);
            }
        };

        List<Plugin> plugins = application.getPlugins();

        assertThat(plugins, contains(plugin1, plugin2));
    }
}
