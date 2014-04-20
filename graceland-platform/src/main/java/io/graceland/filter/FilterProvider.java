package io.graceland.filter;

import javax.servlet.Filter;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;

/**
 * A concrete implementation of {@link com.google.inject.Provider} to help provide Filters.
 *
 * @see io.graceland.filter.FilterBinder
 * @see io.graceland.filter.FilterSpec
 */
public class FilterProvider implements Provider<Filter> {
    private final Filter filter;

    FilterProvider(Filter filter) {
        this.filter = Preconditions.checkNotNull(filter, "Filter cannot be null.");
    }

    @Override
    public Filter get() {
        return filter;
    }
}
