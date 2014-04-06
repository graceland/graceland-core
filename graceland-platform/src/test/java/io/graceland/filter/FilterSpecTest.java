package io.graceland.filter;

import javax.servlet.Filter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FilterSpecTest {

    @Test
    public void has_properties() {
        FilterSpec filterSpec = mock(FilterSpec.class);

        Filter filter = filterSpec.getFilter();
        int prioirty = filterSpec.getPrioirty();
    }

    @Test
    public void builds_correctly() {
        Filter filter = mock(Filter.class);
        int priority = 100;
        FilterSpec filterSpec = new FilterSpec(filter, priority);

        assertThat(filterSpec.getFilter(), is(filter));
        assertThat(filterSpec.getPrioirty(), is(priority));
    }
}
