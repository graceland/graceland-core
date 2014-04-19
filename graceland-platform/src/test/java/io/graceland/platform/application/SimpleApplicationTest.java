package io.graceland.platform.application;

public class SimpleApplicationTest extends ApplicationTest {

    @Override
    protected Application newApplication() {
        return new SimpleApplication() {
            @Override
            protected void configure() {
                // do not load a plugin
            }
        };
    }
}
