package io.graceland.filter;

import javax.servlet.Filter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FilterSpecTest {

    private Filter filter = mock(Filter.class);

    @Test
    public void has_properties() {
        FilterSpec filterSpec = mock(FilterSpec.class);

        Filter filter = filterSpec.getFilter();
        int priority = filterSpec.getPriority();
        String name = filterSpec.getName();
    }

    @Test
    public void builds_with_defaults() {
        FilterSpec filterSpec = FilterSpec
                .forFilter(filter)
                .build();

        assertThat(filterSpec.getFilter(), is(filter));
        assertThat(filterSpec.getName(), is(filter.getClass().getSimpleName()));
        assertThat(filterSpec.getPriority(), is(FilterSpec.DEFAULT_PRIORITY));
    }

    @Test
    public void builds_with_explicits() {
        String name = "my-name";
        int priority = 199;

        FilterSpec filterSpec = FilterSpec
                .forFilter(filter)
                .withName(name)
                .withPriority(priority)
                .build();

        assertThat(filterSpec.getFilter(), is(filter));
        assertThat(filterSpec.getName(), is(name));
        assertThat(filterSpec.getPriority(), is(priority));
    }
}
