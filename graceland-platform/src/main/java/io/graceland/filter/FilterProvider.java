package io.graceland.filter;

import javax.servlet.Filter;

import com.google.inject.Provider;

class FilterProvider implements Provider<Filter> {
    private final Filter filter;

    public FilterProvider(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Filter get() {
        return filter;
    }
}
