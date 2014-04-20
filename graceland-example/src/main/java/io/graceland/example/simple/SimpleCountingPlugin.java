package io.graceland.example.simple;

import com.google.inject.Singleton;

import io.graceland.example.counting.CountingMachine;
import io.graceland.platform.plugin.AbstractPlugin;

public class SimpleCountingPlugin extends AbstractPlugin {

    @Override
    protected void configure() {
        // hook up the counting machine
        bind(CountingMachine.class).to(SimpleCountingMachine.class).in(Singleton.class);
    }
}
