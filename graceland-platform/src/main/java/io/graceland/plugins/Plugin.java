package io.graceland.plugins;

import com.google.inject.Module;

/**
 * A plugin defines the smallest unit of modularity in a Graceland {@link io.graceland.applications.Application}.
 * <p/>
 * It extends the Guice {@link com.google.inject.Module} concept.
 */
public interface Plugin extends Module {
}
