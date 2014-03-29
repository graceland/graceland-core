package io.graceland.application;

import java.util.List;

import org.junit.Test;

import io.graceland.plugin.Plugin;
import io.graceland.testing.TestModes;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ModalApplicationTest extends ApplicationTest {

    private static final String[] ARGS_MISSING = new String[]{"server", "config.yml"};
    private static final String[] ARGS_DEV = new String[]{"server", "config.yml", "--DEV"};
    private static final String[] ARGS_PROD = new String[]{"server", "config.yml", "--PROD"};
    private static final String[] ARGS_NOT_VALID = new String[]{"server", "config.yml", "--NOTFOUND"};
    private static final String[] ARGS_BAD_CASE = new String[]{"server", "config.yml", "--dev"};

    private Plugin plugin0 = mock(Plugin.class);
    private Plugin plugin1 = mock(Plugin.class);
    private Plugin plugin2 = mock(Plugin.class);
    private Plugin plugin3 = mock(Plugin.class);

    @Override
    protected Application newApplication() {
        return new TestModalApplication(ARGS_DEV);
    }

    @Override
    protected Application newEmptyApplication() {
        return new ModalApplication<TestModes>(TestModes.class, TestModes.PROD, ARGS_MISSING) {
            @Override
            protected void configureFor(TestModes mode) {
                // do not load a plugin
            }
        };
    }

    @Test
    public void loads_prod_mode() {
        Application application = new TestModalApplication(ARGS_PROD);

        List<Plugin> plugins = application.getPlugins();

        assertThat(plugins, contains(plugin0, plugin1, plugin2));
    }

    @Test
    public void loads_dev_mode() {
        Application application = new TestModalApplication(ARGS_DEV);

        List<Plugin> plugins = application.getPlugins();

        assertThat(plugins, contains(plugin0, plugin2, plugin3));
    }

    @Test(expected = NullPointerException.class)
    public void requires_a_modeClass() {
        new ModalApplication<TestModes>(null, TestModes.DEV, ARGS_MISSING) {
            @Override
            protected void configureFor(TestModes mode) {
                // do not configure
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void requires_a_default_mode() {
        new ModalApplication<TestModes>(TestModes.class, null, ARGS_MISSING) {
            @Override
            protected void configureFor(TestModes mode) {
                // do not configure
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void requires_args() {
        new ModalApplication<TestModes>(TestModes.class, TestModes.DEV, null) {
            @Override
            protected void configureFor(TestModes mode) {
                // do not configure
            }
        };
    }

    @Test
    public void uses_default_mode_when_none_found() {
        TestModes mode = new TestModalApplication(ARGS_MISSING).getMode();
        assertThat(mode, equalTo(TestModes.PROD));
    }

    @Test
    public void can_read_mode_from_args() {
        TestModes shouldBeDEV = new TestModalApplication(ARGS_DEV).getMode();
        assertThat(shouldBeDEV, equalTo(TestModes.DEV));

        TestModes shouldBePROD = new TestModalApplication(ARGS_PROD).getMode();
        assertThat(shouldBePROD, equalTo(TestModes.PROD));
    }

    @Test(expected = UnknownModeException.class)
    public void throws_error_when_unknown_mode() {
        new TestModalApplication(ARGS_NOT_VALID).getMode();
    }

    @Test(expected = UnknownModeException.class)
    public void modes_are_case_sensitive() {
        new TestModalApplication(ARGS_BAD_CASE).getMode();
    }

    class TestModalApplication extends ModalApplication<TestModes> {

        protected TestModalApplication(String[] args) {
            super(TestModes.class,
                    TestModes.PROD,
                    args);
        }

        @Override
        protected void configureFor(TestModes mode) {
            loadPlugin(plugin0);

            switch (mode) {
                case DEV:
                    loadPlugin(plugin2);
                    loadPlugin(plugin3);
                    break;

                case PROD:
                    loadPlugin(plugin1);
                    loadPlugin(plugin2);
                    break;
            }
        }
    }
}
