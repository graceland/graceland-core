package io.graceland.example.simple;

import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;

import io.graceland.example.counting.Counter;
import io.graceland.example.counting.CountingMachine;

public class SimpleCountingMachine implements CountingMachine {

    private final AtomicLong count = new AtomicLong();

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
