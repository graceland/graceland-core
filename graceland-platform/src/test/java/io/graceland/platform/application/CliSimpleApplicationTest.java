package io.graceland.platform.application;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import io.graceland.platform.plugin.AbstractPlugin;
import io.graceland.platform.plugin.Plugin;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CliSimpleApplicationTest extends ApplicationTest {
    @Override
    protected Application newApplication() {
        String[] args = new String[]{};
        return new CliSimpleApplication(args) {
            @Override
            protected void configure() {
                System.out.println("My CLI Arguments: " + getCommandLineArguments());
            }
        };
    }

    @Test
    public void can_provide_command_line_arguments() {
        CliSimpleApplication cliApplication = mock(CliSimpleApplication.class);
        ImmutableList<String> args = cliApplication.getCommandLineArguments();
    }

    // TODO: (jmc) there is tight coupling showing up here that i'm not happy with. platform and application need work.
    @Test
    public void can_provide_cli_args_to_plugin_constructors() throws Exception {
        // TODO: (jmc) clean up when it's a list and when it's an array
        String[] args = new String[]{"server", "example.yml", "other", "--param"};
        ImmutableList<String> argsList = ImmutableList.of("server", "example.yml", "other", "--param");

        final ArgsVerifyer argsVerifyer = mock(ArgsVerifyer.class);

        CliSimpleApplication app = new CliSimpleApplication(args) {
            @Override
            protected void configure() {
                Plugin plugin = new ArgsInConstructorPlugin(getCommandLineArguments(), argsVerifyer);
                loadPlugin(plugin);
            }
        };

        app.getPlugins();
        verify(argsVerifyer).canAccessArgs(eq(argsList));
    }

    // used only to mock and verify
    interface ArgsVerifyer {
        void canAccessArgs(ImmutableList<String> args);
    }

    class ArgsInConstructorPlugin extends AbstractPlugin {
        private final ImmutableList<String> args;
        private final ArgsVerifyer argsVerifyer;

        // pass in the command args
        ArgsInConstructorPlugin(ImmutableList<String> args, ArgsVerifyer argsVerifyer) {
            this.args = args;
            this.argsVerifyer = argsVerifyer;

            // verify it's in the plugin
            argsVerifyer.canAccessArgs(args);
        }

        @Override
        protected void configure() {
        }

        public ArgsVerifyer getArgsVerifyer() {
            return argsVerifyer;
        }
    }
}
