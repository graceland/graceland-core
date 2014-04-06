package io.graceland.filter;

import javax.servlet.Filter;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;

import io.graceland.inject.Graceland;

public class FilterBinder {
    private final Provider<? extends Filter> filterProvider;
    private final Binder binder;
    private final Class<? extends Filter> filterClass;

    // TODO: move from FilterSpec to this class
    private int priority = FilterSpec.DEFAULT_PRIORITY;
    // TODO: change this to an optional
    private String name = null;

    FilterBinder(
            Binder binder,
            Class<? extends Filter> filterClass,
            Provider<? extends Filter> filterProvider) {

        this.binder = binder;
        this.filterClass = filterClass;
        this.filterProvider = filterProvider;
    }

    public static FilterBinder forInstance(Binder binder, Filter filter) {
        return new FilterBinder(binder, filter.getClass(), new FilterProvider(filter));
    }

    public static FilterBinder forClass(
            Binder binder,
            Class<? extends Filter> filterClass,
            Provider<? extends Filter> provider) {

        return new FilterBinder(binder, filterClass, provider);
    }

    public FilterBinder withPriority(int filterPriority) {
        this.priority = filterPriority;
        return this;
    }

    public FilterBinder withName(String filterName) {
        this.name = filterName;
        return this;
    }

    public void bind() {
        if (name == null) {
            // name = filter.getClass().getSimpleName();
            name = filterClass.getSimpleName();
        }

        FilterSpec fitlerSpec = new FilterSpec(filterProvider, priority, name);

        Multibinder
                .newSetBinder(binder, FilterSpec.class, Graceland.class)
                .addBinding()
                .toInstance(fitlerSpec);
    }
}
