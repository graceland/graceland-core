package io.graceland.filter;

import org.junit.Test;
import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.graceland.inject.Keys;
import io.graceland.testing.TestFilter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class FilterBinderTest {

    private final TestFilter filter = new TestFilter();
    private final int filterPriority = 999;
    private final String filterName = "my-filter-filterName";

    @Test
    public void binds_from_instance() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                FilterBinder
                        .forInstance(binder(), filter)
                        .withPriority(filterPriority)
                        .withName(filterName)
                        .bind();
            }
        };

        verifyInjectedValue(abstractModule, filterPriority, filterName);
    }

    @Test
    public void binds_from_class() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                FilterBinder
                        .forClass(binder(), TestFilter.class)
                        .withPriority(filterPriority)
                        .withName(filterName)
                        .bind();
            }
        };

        verifyInjectedValue(abstractModule, filterPriority, filterName);
    }

    @Test
    public void uses_default_values() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                FilterBinder.forClass(binder(), TestFilter.class).bind();
            }
        };

        verifyInjectedValue(abstractModule, FilterBinder.DEFAULT_PRIORITY, TestFilter.class.getSimpleName());
    }

    private void verifyInjectedValue(AbstractModule abstractModule, int priority, String name) {
        Injector injector = Guice.createInjector(abstractModule);

        FilterSpec filterSpec = Iterables.getFirst(injector.getInstance(Keys.FilterSpecs), null);

        assertThat(filterSpec, is(not(nullValue())));
        assertThat(filterSpec.getPriority(), is(priority));
        assertThat(filterSpec.getName(), is(name));
    }
}
