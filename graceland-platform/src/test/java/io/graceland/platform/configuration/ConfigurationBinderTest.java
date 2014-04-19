package io.graceland.platform.configuration;

import org.junit.Test;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.graceland.platform.testing.TestAnnotation;
import io.graceland.platform.testing.TestConfiguration;
import io.graceland.platform.testing.TestFileConfiguration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ConfigurationBinderTest {

    @Test(expected = NullPointerException.class)
    public void constructor_class_cannot_be_null() {
        ConfigurationBinder.forClass(null, mock(Binder.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_binder_cannot_be_null() {
        ConfigurationBinder.forClass(TestConfiguration.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void toInstance_cannot_bind_null() {
        ConfigurationBinder
                .forClass(TestConfiguration.class, mock(Binder.class))
                .toInstance(null);
    }

    @Test
    public void toInstance_binds_an_instance() {
        final TestConfiguration expectedConfiguration = new TestConfiguration();

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ConfigurationBinder
                        .forClass(TestConfiguration.class, binder())
                        .toInstance(expectedConfiguration);
            }
        });

        assertThat(
                injector.getInstance(Key.get(TestConfiguration.class)),
                is(expectedConfiguration));
    }

    @Test
    public void toInstance_binds_an_annotated_instance() {
        final TestConfiguration expectedConfiguration = new TestConfiguration();

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ConfigurationBinder
                        .forClass(TestConfiguration.class, binder())
                        .annotatedWith(TestAnnotation.class).toInstance(expectedConfiguration);
            }
        });

        assertThat(
                injector.getInstance(Key.get(TestConfiguration.class, TestAnnotation.class)),
                is(expectedConfiguration));
    }

    @Test(expected = NullPointerException.class)
    public void toFile_cannot_bind_null() {
        ConfigurationBinder
                .forClass(TestConfiguration.class, mock(Binder.class))
                .toFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toFile_file_must_exist() {
        ConfigurationBinder
                .forClass(TestFileConfiguration.class, mock(Binder.class))
                .toFile("file/does/not/exist.yml");
    }

    @Test
    public void toFile_binds_to_a_file() {
        final TestFileConfiguration expectedConfiguration = new TestFileConfiguration(123, "xyz");

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ConfigurationBinder
                        .forClass(TestFileConfiguration.class, binder())
                        .toFile("src/test/resources/fixtures/io/graceland/configuration/ConfigurationBinder/test.yml");
            }
        });

        assertThat(
                injector.getInstance(Key.get(TestFileConfiguration.class)),
                equalTo(expectedConfiguration));
    }

    @Test
    public void toFile_binds_to_an_annotated_file() {
        final TestFileConfiguration expectedConfiguration = new TestFileConfiguration(123, "xyz");

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                ConfigurationBinder
                        .forClass(TestFileConfiguration.class, binder())
                        .annotatedWith(TestAnnotation.class)
                        .toFile("src/test/resources/fixtures/io/graceland/configuration/ConfigurationBinder/test.yml");
            }
        });

        assertThat(
                injector.getInstance(Key.get(TestFileConfiguration.class, TestAnnotation.class)),
                equalTo(expectedConfiguration));
    }
}
