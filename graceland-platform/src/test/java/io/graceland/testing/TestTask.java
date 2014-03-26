package io.graceland.testing;

import java.io.PrintWriter;

import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;

public class TestTask extends Task {
    public TestTask() {
        super("test-task");
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        // do nothing
    }
}
