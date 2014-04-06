package io.graceland.filter;

import javax.servlet.Filter;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;

class FilterProvider implements Provider<Filter> {
    private final Filter filter;

    public FilterProvider(Filter filter) {
        this.filter = Preconditions.checkNotNull(filter, "Filter cannot be null.");
    }

    @Override
    public Filter get() {
        return filter;
    }
}
