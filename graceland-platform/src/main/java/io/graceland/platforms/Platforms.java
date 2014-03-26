package io.graceland.platforms;

import com.google.common.base.Preconditions;

import io.graceland.applications.Application;

/**
 * A utility class that builds a platform for a particular scenario.
 */
public final class Platforms {

    protected Platforms() {
        // utility class
    }

    /**
     * The simplest platform scenario - uses the {@link io.graceland.platforms.DefaultPlatform}.
     *
     * @param application The application to use.
     * @return A working {@link io.graceland.platforms.DefaultPlatform}.
     */
    public static Platform forApplication(Application application) {
        Preconditions.checkNotNull(application, "Application cannot be null.");
        return new DefaultPlatform(application);
    }
}
