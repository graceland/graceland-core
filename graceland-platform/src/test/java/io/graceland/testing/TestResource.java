package io.graceland.testing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api")
public class TestResource {
    @GET
    public String hello() {
        return "world";
    }
}
