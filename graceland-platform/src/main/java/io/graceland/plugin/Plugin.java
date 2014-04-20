package io.graceland.plugin;

import com.google.inject.Module;

/**
 * A plugin defines the smallest unit of modularity in a Graceland {@link io.graceland.application.Application}.
 * <p/>
 * It extends the Guice {@link com.google.inject.Module} concept.
 */
public interface Plugin extends Module {
}
