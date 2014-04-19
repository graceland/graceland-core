package io.graceland.example.startingon;

import com.google.inject.Singleton;

import io.graceland.example.counting.CountingMachine;
import io.graceland.platform.plugin.AbstractPlugin;

public class StartingOnCountingPlugin extends AbstractPlugin {

    @Override
    protected void configure() {
        // hook up the counting machine
        bind(CountingMachine.class).to(StartingOnCountingMachine.class).in(Singleton.class);

        // bind the configuration file to the class
        bindConfiguration(StartingOnConfiguration.class).toFile("starting-on.yml");
    }
}
