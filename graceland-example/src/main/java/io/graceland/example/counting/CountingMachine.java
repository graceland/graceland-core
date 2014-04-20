package io.graceland.example.counting;

public interface CountingMachine {

    void increment();

    void resetCount();

    Counter getCurrentCount();
}
