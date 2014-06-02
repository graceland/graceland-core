package io.graceland.platform.application;

import com.google.common.collect.ImmutableList;

public abstract class CliSimpleApplication extends SimpleApplication {

    protected final ImmutableList<String> args;

    public CliSimpleApplication(String[] args) {
        this.args = ImmutableList.copyOf(args);
    }

    public ImmutableList<String> getCommandLineArguments() {
        return args;
    }
}
