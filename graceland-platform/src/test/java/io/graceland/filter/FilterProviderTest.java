package io.graceland.filter;

import javax.servlet.Filter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FilterProviderTest {

    @Test(expected = NullPointerException.class)
    public void filter_cannot_be_null() {
        new FilterProvider(null);
    }

    @Test
    public void returns_instance_passed_in() {
        Filter filter = mock(Filter.class);
        FilterProvider filterProvider = new FilterProvider(filter);

        assertThat(filterProvider.get(), is(filter));
    }
}
