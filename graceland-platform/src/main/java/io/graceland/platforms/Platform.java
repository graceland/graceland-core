package io.graceland.platforms;

/**
 * The platform is the foundation for Graceland. It's where an {@link io.graceland.applications.Application}
 * is ran, providing the underlying functionality using a Dropwizard service (via an {@link io.dropwizard.Application}).
 */
public interface Platform {

    /**
     * Starts the platform.
     * <p/>
     * It will start up the Dropwizard service, calling {@link io.dropwizard.Application#run(String[])} with the
     * arguments passed in.
     *
     * @param args The command line arguments.
     * @throws Exception Starting up a Dropwizard service may throw an exception.
     */
    void start(String[] args) throws Exception;
}
