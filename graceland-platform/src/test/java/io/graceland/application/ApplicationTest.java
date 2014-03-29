package io.graceland.application;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import io.graceland.plugins.Plugin;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public abstract class ApplicationTest {

    protected abstract Application newApplication();

    protected abstract Application newEmptyApplication();

    @Test
    public void getPlugins_stay_the_same() {
        Application application = newApplication();

        ImmutableList<Plugin> plugins1 = application.getPlugins();
        ImmutableList<Plugin> plugins2 = application.getPlugins();

        assertThat(plugins1, equalTo(plugins2));
    }

    @Test(expected = IllegalStateException.class)
    public void must_load_at_least_one_plugin() {
        Application application = newEmptyApplication();
        application.getPlugins();
    }
}
