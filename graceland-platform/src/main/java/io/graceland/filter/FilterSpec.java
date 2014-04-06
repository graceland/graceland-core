package io.graceland.filter;

import java.util.Comparator;
import javax.servlet.Filter;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;

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

    FilterSpec(
            Provider<? extends Filter> filterProvider,
            int priority,
            String name) {

        this.filterProvider = Preconditions.checkNotNull(filterProvider, "Filter Provider cannot be null.");
        this.priority = Preconditions.checkNotNull(priority, "Priority cannot be null.");
        this.name = Preconditions.checkNotNull(name, "Name cannot be null.");
    }

    public Filter getFilter() {
        return filterProvider.get();
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }
}
