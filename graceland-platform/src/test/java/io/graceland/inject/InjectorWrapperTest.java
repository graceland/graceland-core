package io.graceland.inject;

import org.junit.Test;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.graceland.filter.FilterSpec;
import io.graceland.plugin.AbstractPlugin;
import io.graceland.testing.TestBundle;
import io.graceland.testing.TestCommand;
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

                        bindFilter(mock(FilterSpec.class));
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
        assertThat(wrapper.getFilters().size(), is(1));
    }
}
