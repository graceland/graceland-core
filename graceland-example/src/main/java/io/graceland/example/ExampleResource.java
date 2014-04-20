package io.graceland.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.graceland.example.counting.Counter;
import io.graceland.example.counting.CountingMachine;

@Path("/api/example")
public class ExampleResource {

    private final CountingMachine countingMachine;

    @Inject
    ExampleResource(CountingMachine countingMachine) {
        this.countingMachine = countingMachine;
    }

    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Counter getCurrentCount() {
        countingMachine.increment();
        return countingMachine.getCurrentCount();
    }
}
