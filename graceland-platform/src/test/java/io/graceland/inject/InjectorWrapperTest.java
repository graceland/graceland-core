package io.graceland.inject;

import javax.servlet.Filter;

import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.graceland.filter.FilterSpec;
import io.graceland.plugin.AbstractPlugin;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
import io.graceland.testing.TestFilter;
import io.graceland.testing.TestHealthCheck;
import io.graceland.testing.TestManaged;
import io.graceland.testing.TestTask;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InjectorWrapperTest {

    @Test
    public void build_with_an_injector() {
        Injector injector = mock(Injector.class);
        InjectorWrapper.wrap(injector);
    }

    @Test(expected = NullPointerException.class)
    public void injector_cannot_be_null() {
        InjectorWrapper.wrap(null);
    }

    @Test
    public void returns_empty_sets_when_nothing_bound() {
        Injector injector = Guice.createInjector();
        InjectorWrapper wrapper = InjectorWrapper.wrap(injector);

        assertThat(wrapper.getJerseyComponents(), is(empty()));
        assertThat(wrapper.getHealthChecks(), is(empty()));
        assertThat(wrapper.getTasks(), is(empty()));
        assertThat(wrapper.getManaged(), is(empty()));
        assertThat(wrapper.getBundles(), is(empty()));
        assertThat(wrapper.getCommands(), is(empty()));
    }

    @Test
    public void returns_items_from_both_a_class_and_an_instance() {
        Injector injector = Guice.createInjector(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        bindJerseyComponent(new Object());
                        bindJerseyComponent(Object.class);

                        bindHealthCheck(new TestHealthCheck());
                        bindHealthCheck(TestHealthCheck.class);

                        bindTask(new TestTask());
                        bindTask(TestTask.class);

                        bindManaged(new TestManaged());
                        bindManaged(TestManaged.class);

                        bindBundle(new TestBundle());
                        bindBundle(TestBundle.class);

                        bindCommand(new TestCommand());
                        bindCommand(TestCommand.class);

                        buildFilter(new TestFilter()).bind();
                        buildFilter(TestFilter.class).bind();
                    }
                }
        );

        InjectorWrapper wrapper = InjectorWrapper.wrap(injector);

        assertThat(wrapper.getJerseyComponents().size(), is(2));
        assertThat(wrapper.getHealthChecks().size(), is(2));
        assertThat(wrapper.getTasks().size(), is(2));
        assertThat(wrapper.getManaged().size(), is(2));
        assertThat(wrapper.getBundles().size(), is(2));
        assertThat(wrapper.getCommands().size(), is(2));
        assertThat(wrapper.getFilterSpecs().size(), is(2));
    }

    @Test
    public void returns_filters_in_priority_order() {
        final Filter filter1 = mock(Filter.class);
        final Filter filter2 = mock(Filter.class);
        final Filter filter3 = mock(Filter.class);

        Injector injector = Guice.createInjector(
                new AbstractPlugin() {
                    @Override
                    protected void configure() {
                        buildFilter(filter2).withPriority(0).bind();
                        buildFilter(filter1).withPriority(-10).bind();
                        buildFilter(filter3).withPriority(100).bind();
                    }
                }
        );

        InjectorWrapper wrapper = InjectorWrapper.wrap(injector);

        ImmutableList<FilterSpec> filterSpecs = wrapper.getFilterSpecs();

        assertThat(filterSpecs.get(0).getFilter(), is(filter1));
        assertThat(filterSpecs.get(1).getFilter(), is(filter2));
        assertThat(filterSpecs.get(2).getFilter(), is(filter3));
    }
}
