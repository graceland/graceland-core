package io.graceland.filter;

import javax.servlet.Filter;

import org.junit.Test;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterSpecTest {

    @Test
    public void sets_values_correctly() {
        Filter filter = mock(Filter.class);
        Provider<? extends Filter> filterProvider = mock(Provider.class);
        when(filterProvider.get()).thenReturn(filter);

        int priority = 100;
        String name = "my-name";

        FilterSpec filterSpec = new FilterSpec(filterProvider, priority, name);

        assertThat(filterSpec.getFilter(), is(filter));
        assertThat(filterSpec.getPriority(), is(priority));
        assertThat(filterSpec.getName(), is(name));
    }

    @Test
    public void orders_ascending_using_priority() {
        FilterSpec filterSpec1 = new FilterSpec(mock(FilterProvider.class), -100, "first");
        FilterSpec filterSpec2 = new FilterSpec(mock(FilterProvider.class), 0, "second");
        FilterSpec filterSpec3 = new FilterSpec(mock(FilterProvider.class), 100, "third");

        ImmutableList<FilterSpec> outOfOrder = ImmutableList.of(filterSpec2, filterSpec3, filterSpec1);

        ImmutableList<FilterSpec> ordered = FluentIterable
                .from(outOfOrder)
                .toSortedList(FilterSpec.PRIORITY_COMPARATOR);

        assertThat(ordered, contains(filterSpec1, filterSpec2, filterSpec3));
    }
}
