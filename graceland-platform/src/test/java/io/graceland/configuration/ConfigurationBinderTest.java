package io.graceland.configuration;

import org.junit.Test;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.graceland.testing.TestConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ConfigurationBinderTest {

    @Test(expected = NullPointerException.class)
    public void constructor_class_cannot_be_null() {
        new ConfigurationBinder<TestConfiguration>(null, mock(Binder.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_binder_cannot_be_null() {
        new ConfigurationBinder<TestConfiguration>(TestConfiguration.class, null);
    }

    @Test
    public void toInstance_binds_an_instance() {
        final TestConfiguration expectedConfiguration = new TestConfiguration();

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ConfigurationBinder<TestConfiguration> configurationBinder = ConfigurationBinder.forClass(TestConfiguration.class, binder());
                configurationBinder.toInstance(expectedConfiguration);
            }
        });

        TestConfiguration actualConfiguration = injector.getInstance(TestConfiguration.class);

        assertThat(actualConfiguration, is(expectedConfiguration));
    }
}
