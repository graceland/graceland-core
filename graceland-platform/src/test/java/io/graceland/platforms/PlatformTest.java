package io.graceland.platforms;

import org.junit.Test;

import io.graceland.applications.Application;

import static org.mockito.Mockito.mock;

public abstract class PlatformTest {

    protected abstract Platform newPlatform(Application application);

    @Test(expected = NullPointerException.class)
    public void start_must_be_called_with_args() throws Exception {
        Application application = mock(Application.class);
        Platform platform = newPlatform(application);
        platform.start(null);
    }
}
