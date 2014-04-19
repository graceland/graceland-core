package io.graceland.platform.filter;

import java.util.Comparator;
import javax.servlet.Filter;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;

/**
 * A filter specification, used when adding a filter to the {@link io.dropwizard.jetty.setup.ServletEnvironment}. It
 * provides the filter instance, the priority of the filter, and a name for the filter.
 */
public class FilterSpec {
    public static final Comparator<FilterSpec> PRIORITY_COMPARATOR = new Comparator<FilterSpec>() {
        @Override
        public int compare(FilterSpec o1, FilterSpec o2) {
            return o1.getPriority() - o2.getPriority();
        }
    };

    private final Provider<? extends Filter> filterProvider;
    private final int priority;
    private final String name;
    private final ImmutableList<FilterPattern> patterns;

    FilterSpec(
            Provider<? extends Filter> filterProvider,
            int priority,
            String name,
            ImmutableList<FilterPattern> patterns) {

        this.filterProvider = Preconditions.checkNotNull(filterProvider, "Filter Provider cannot be null.");
        this.priority = Preconditions.checkNotNull(priority, "Priority cannot be null.");
        this.name = Preconditions.checkNotNull(name, "Name cannot be null.");
        this.patterns = Preconditions.checkNotNull(patterns, "Filter Patterns cannot be null.");
    }

    public Filter getFilter() {
        return filterProvider.get();
    }

    /**
     * The priority is used to determine the order of the filters added to the {@link io.dropwizard.setup.Environment}.
     *
     * @return The filter's priority.
     */
    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public ImmutableList<FilterPattern> getPatterns() {
        return patterns;
    }
}
