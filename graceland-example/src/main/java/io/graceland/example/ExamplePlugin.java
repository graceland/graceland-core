package io.graceland.example;

import io.graceland.platform.plugin.AbstractPlugin;

public class ExamplePlugin extends AbstractPlugin {

    @Override
    protected void configure() {
        // add the resource
        bindJerseyComponent(ExampleResource.class);

        // add the task
        bindTask(ResetTask.class);
    }
}
