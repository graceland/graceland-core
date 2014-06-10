package io.graceland.platform;

/**
 * The platform is the foundation for Graceland. It's where an {@link io.graceland.platform.application.Application}
 * is ran, providing the underlying functionality.
 */
public interface Platform {

    /**
     * Starts the platform.
     *
     * @param args The command line arguments.
     * @throws Exception The platform can throw any exception.
     * @see io.graceland.platform.dropwizard.DropwizardPlatform
     */
    void start(String[] args) throws Exception;
}
