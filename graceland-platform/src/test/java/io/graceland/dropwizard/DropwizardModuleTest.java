package io.graceland.dropwizard;

import org.junit.Test;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.dropwizard.setup.Environment;
import io.graceland.inject.Graceland;
import io.graceland.PlatformConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DropwizardModuleTest {

    private PlatformConfiguration configuration = mock(PlatformConfiguration.class);
    private Environment environment = mock(Environment.class);
    private DropwizardModule dropwizardModule = new DropwizardModule();

    @Test(expected = NullPointerException.class)
    public void setup_cannot_take_null_configuration() {
        dropwizardModule.setup(null, environment);
    }

    @Test(expected = NullPointerException.class)
    public void setup_cannot_take_null_environment() {
        dropwizardModule.setup(configuration, null);
    }

    @Test(expected = IllegalStateException.class)
    public void must_set_configuration_before_providing_config() {
        dropwizardModule.providePlatformConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void must_set_environment_before_providing_object_mapper() {
        dropwizardModule.provideObjectMapper();
    }

    @Test(expected = IllegalStateException.class)
    public void must_set_environment_before_providing_metric_registry() {
        dropwizardModule.provideMetricRegistry();
    }

    @Test
    public void use_environment_to_provide_object_mapper() {
        dropwizardModule.setup(configuration, environment);
        dropwizardModule.provideObjectMapper();
        verify(environment).getObjectMapper();
    }

    @Test
    public void use_environment_to_provide_metric_registry() {
        dropwizardModule.setup(configuration, environment);
        dropwizardModule.provideMetricRegistry();
        verify(environment).metrics();
    }

    @Test
    public void provides_dropwizard_components() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        MetricRegistry metricRegistry = mock(MetricRegistry.class);

        when(environment.getObjectMapper()).thenReturn(objectMapper);
        when(environment.metrics()).thenReturn(metricRegistry);

        dropwizardModule.setup(configuration, environment);
        Injector injector = Guice.createInjector(dropwizardModule);

        PlatformConfiguration actualConfiguration = injector.getInstance(Key.get(PlatformConfiguration.class, Graceland.class));
        ObjectMapper actualObjectMapper = injector.getInstance(Key.get(ObjectMapper.class, Graceland.class));
        MetricRegistry actualMetricRegistry = injector.getInstance(Key.get(MetricRegistry.class, Graceland.class));

        assertThat(actualConfiguration, is(configuration));
        assertThat(actualObjectMapper, is(objectMapper));
        assertThat(actualMetricRegistry, is(metricRegistry));
    }
}
