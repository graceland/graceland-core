package io.graceland.testing;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class TestCommand extends Command {
    public TestCommand() {
        super("name", "description");
    }

    @Override
    public void configure(Subparser subparser) {
        // do nothing
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        // do nothing
    }
}
