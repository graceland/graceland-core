package io.graceland.platform.testing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api")
public class TestResource {
    @GET
    public String hello() {
        return "world";
    }
}
