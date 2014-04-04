package io.graceland.example;

import io.graceland.Platform;
import io.graceland.application.SimpleApplication;
import io.graceland.example.startingon.StartingOnCountingPlugin;

public class ExampleApplication extends SimpleApplication {

    public static void main(String[] args) throws Exception {
        Platform
                .forApplication(new ExampleApplication())
                .start(args);
    }

    @Override
    protected void configure() {
        loadPlugin(new ExamplePlugin());

        // choose between simple or starting-on versions
        loadPlugin(new StartingOnCountingPlugin());
        // loadPlugin(new SimpleCountingPlugin());
    }
}
