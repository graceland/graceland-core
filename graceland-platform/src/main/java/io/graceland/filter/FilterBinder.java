package io.graceland.filter;

import javax.servlet.Filter;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;

import io.graceland.inject.Graceland;

/**
 * Used to create and bind a {@link io.graceland.filter.FilterSpec}.
 *
 * @see io.graceland.filter.FilterSpec
 * @see io.graceland.filter.FilterProvider
 */
public class FilterBinder {
    public static final int DEFAULT_PRIORITY = 500;
    public static final FilterPattern DEFAULT_PATTERN = FilterPattern.forPatterns("/*");

    private final Binder binder;
    private final Class<? extends Filter> filterClass;
    private final Provider<? extends Filter> filterProvider;
    private final ImmutableList.Builder<FilterPattern> filterPatterns;

    private int priority = DEFAULT_PRIORITY;
    private Optional<String> name = Optional.absent();

    FilterBinder(
            Binder binder,
            Class<? extends Filter> filterClass,
            Provider<? extends Filter> filterProvider) {

        this.binder = Preconditions.checkNotNull(binder, "Binder cannot be null.");
        this.filterClass = Preconditions.checkNotNull(filterClass, "Filter Class cannot be null.");
        this.filterProvider = Preconditions.checkNotNull(filterProvider, "Filter Provider cannot be null.");
        this.filterPatterns = ImmutableList.builder();
    }

    /**
     * Builds a Filter Binder using a Filter instance rather than a class. It will use a
     * {@link io.graceland.filter.FilterProvider} as the provider.
     *
     * @param binder The Binder for the current Guice module.
     * @param filter The Filter to be bound.
     * @return A working FilterBinder.
     */
    public static FilterBinder forInstance(Binder binder, Filter filter) {
        Preconditions.checkNotNull(filter, "Filter cannot be null.");

        return new FilterBinder(binder, filter.getClass(), new FilterProvider(filter));
    }

    /**
     * Builds a Filter Binder using a Filter Class rather than an instance. It will use the {@link com.google.inject.Binder}
     * to create a {@link com.google.inject.Provider} used in the Filter Binder.
     *
     * @param binder      The Binder for the current Guice module.
     * @param filterClass The Class of a Filter to be bound.
     * @return A working FilterBinder.
     */
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

    /**
     * Sets the priority of the filter. If none is set, the {@link io.graceland.filter.FilterBinder#DEFAULT_PRIORITY}
     * is used.
     *
     * @param filterPriority The priority to use for the filter.
     * @return The current Filter Binder.
     */
    public FilterBinder withPriority(int filterPriority) {
        this.priority = filterPriority;
        return this;
    }

    /**
     * Sets the name of the filter. If none is set, the filter's class simple name will be used.
     *
     * @param filterName The name to use for the filter.
     * @return The current Filter Binder.
     */
    public FilterBinder withName(String filterName) {
        this.name = Optional.fromNullable(filterName);
        return this;
    }

    public FilterBinder withPattern(FilterPattern filterPattern) {
        Preconditions.checkNotNull(filterPattern, "Filter Pattern cannot be null.");
        filterPatterns.add(filterPattern);
        return this;
    }

    /**
     * Builds a {@link io.graceland.filter.FilterSpec} and adds it to the Guice dependency graph.
     */
    public void bind() {
        FilterSpec fitlerSpec = new FilterSpec(
                filterProvider,
                priority,
                name.or(filterClass.getSimpleName()),
                buildPatterns());

        Multibinder
                .newSetBinder(binder, FilterSpec.class, Graceland.class)
                .addBinding()
                .toInstance(fitlerSpec);
    }

    private ImmutableList<FilterPattern> buildPatterns() {
        ImmutableList<FilterPattern> patterns = filterPatterns.build();

        if (patterns.isEmpty()) {
            patterns = ImmutableList.of(FilterBinder.DEFAULT_PATTERN);
        }

        return patterns;
    }
}
