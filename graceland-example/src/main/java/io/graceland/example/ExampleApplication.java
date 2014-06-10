package io.graceland.example;

import io.graceland.example.startingon.StartingOnCountingPlugin;
import io.graceland.platform.DropwizardPlatform;
import io.graceland.platform.Platform;
import io.graceland.platform.application.SimpleApplication;

public class ExampleApplication extends SimpleApplication {

    public static void main(String[] args) throws Exception {
        Platform platform = DropwizardPlatform.forApplication(new ExampleApplication());
        platform.start(args);
    }

    @Override
    protected void configure() {
        loadPlugin(new ExamplePlugin());

        // choose between simple or starting-on versions
        loadPlugin(new StartingOnCountingPlugin());
        // loadPlugin(new SimpleCountingPlugin());
    }
}
