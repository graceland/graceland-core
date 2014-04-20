package io.graceland.platform.filter;

import org.junit.Test;
import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.graceland.platform.inject.Keys;
import io.graceland.platform.testing.TestFilter;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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

        verifyInjectedValue(abstractModule, filterPriority, filterName, FilterBinder.DEFAULT_PATTERN);
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

        verifyInjectedValue(abstractModule, filterPriority, filterName, FilterBinder.DEFAULT_PATTERN);
    }

    @Test
    public void uses_default_values() {
        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                FilterBinder.forClass(binder(), TestFilter.class).bind();
            }
        };

        verifyInjectedValue(
                abstractModule,
                FilterBinder.DEFAULT_PRIORITY,
                TestFilter.class.getSimpleName(),
                FilterBinder.DEFAULT_PATTERN);
    }

    @Test
    public void can_add_more_than_one_patterns() {
        final FilterPattern pattern1 = mock(FilterPattern.class);
        final FilterPattern pattern2 = mock(FilterPattern.class);

        AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                FilterBinder
                        .forClass(binder(), TestFilter.class)
                        .withPriority(filterPriority)
                        .addPattern(pattern1)
                        .addPattern(pattern2)
                        .withName(filterName)
                        .bind();
            }
        };

        verifyInjectedValue(abstractModule, filterPriority, filterName, pattern1, pattern2);
    }

    private void verifyInjectedValue(
            AbstractModule abstractModule,
            int priority,
            String name,
            FilterPattern... patterns) {

        Injector injector = Guice.createInjector(abstractModule);

        FilterSpec filterSpec = Iterables.getFirst(injector.getInstance(Keys.FilterSpecs), null);

        assertThat(filterSpec, is(not(nullValue())));
        assertThat(filterSpec.getPriority(), is(priority));
        assertThat(filterSpec.getName(), is(name));

        assertThat(filterSpec.getPatterns(), containsInAnyOrder(patterns));
    }
}
