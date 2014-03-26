package io.graceland.testing;

import com.codahale.metrics.health.HealthCheck;

public class TestHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
