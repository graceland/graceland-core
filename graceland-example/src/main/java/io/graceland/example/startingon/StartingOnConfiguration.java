package io.graceland.example.startingon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.graceland.platform.configuration.Configuration;

public class StartingOnConfiguration implements Configuration {

    private final long startingOn;

    @JsonCreator
    public StartingOnConfiguration(
            @JsonProperty("startingOn") long startingOn) {

        this.startingOn = startingOn;
    }

    public long getStartingOn() {
        return startingOn;
    }
}
