package io.graceland.example.counting;

import org.joda.time.DateTime;

public class Counter {
    private final long count;
    private final DateTime timestamp;

    public Counter(long count, DateTime timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }

    public long getCount() {
        return count;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
