package io.graceland.filter;

import javax.servlet.Filter;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;

import io.graceland.inject.Graceland;

public class FilterBinder {
    public static final int DEFAULT_PRIORITY = 500;

    private final Binder binder;
    private final Class<? extends Filter> filterClass;
    private final Provider<? extends Filter> filterProvider;

    private int priority = DEFAULT_PRIORITY;
    private Optional<String> name = Optional.absent();

    FilterBinder(
            Binder binder,
            Class<? extends Filter> filterClass,
            Provider<? extends Filter> filterProvider) {

        this.binder = Preconditions.checkNotNull(binder, "Binder cannot be null.");
        this.filterClass = Preconditions.checkNotNull(filterClass, "Filter Class cannot be null.");
        this.filterProvider = Preconditions.checkNotNull(filterProvider, "Filter Provider cannot be null.");
    }

    public static FilterBinder forInstance(Binder binder, Filter filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null.");

        return new FilterBinder(binder, filter.getClass(), new FilterProvider(filter));
    }

    public static FilterBinder forClass(
            Binder binder,
            Class<? extends Filter> filterClass) {

        Preconditions.checkNotNull(binder, "Binder cannot be null.");
        Preconditions.checkNotNull(filterClass, "Filter Class cannot be null.");

        return new FilterBinder(
                binder,
                filterClass,
                binder.getProvider(filterClass));
    }

    public FilterBinder withPriority(int filterPriority) {
        this.priority = filterPriority;
        return this;
    }

    public FilterBinder withName(String filterName) {
        this.name = Optional.fromNullable(filterName);
        return this;
    }

    public void bind() {
        FilterSpec fitlerSpec = new FilterSpec(
                filterProvider,
                priority,
                name.or(filterClass.getSimpleName()));

        Multibinder
                .newSetBinder(binder, FilterSpec.class, Graceland.class)
                .addBinding()
                .toInstance(fitlerSpec);
    }
}
