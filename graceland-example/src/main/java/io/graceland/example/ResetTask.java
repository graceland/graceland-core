package io.graceland.example;

import java.io.PrintWriter;
import javax.inject.Inject;

import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;
import io.graceland.example.counting.CountingMachine;

public class ResetTask extends Task {

    private final CountingMachine countingMachine;

    @Inject
    ResetTask(CountingMachine countingMachine) {
        super("reset");

        this.countingMachine = countingMachine;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> stringStringImmutableMultimap, PrintWriter printWriter) throws Exception {
        countingMachine.resetCount();

        printWriter.println("Count Reset!");
        printWriter.flush();
    }
}
