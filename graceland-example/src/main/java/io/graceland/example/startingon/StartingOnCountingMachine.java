package io.graceland.example.startingon;

import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;

import org.joda.time.DateTime;

import io.graceland.example.counting.Counter;
import io.graceland.example.counting.CountingMachine;

public class StartingOnCountingMachine implements CountingMachine {

    private final AtomicLong count;

    @Inject
    StartingOnCountingMachine(StartingOnConfiguration configuration) {
        // use the configuration to get the starting on count
        count = new AtomicLong(configuration.getStartingOn());
    }

    @Override
    public void increment() {
        count.incrementAndGet();
    }

    @Override
    public void resetCount() {
        count.set(0);
    }

    @Override
    public Counter getCurrentCount() {
        return new Counter(count.get(), DateTime.now());
    }
}
