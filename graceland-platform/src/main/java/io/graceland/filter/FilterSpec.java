package io.graceland.filter;

import java.util.Comparator;
import javax.servlet.Filter;

import com.google.inject.Provider;

public class FilterSpec {
    public static final int DEFAULT_PRIORITY = 500;
    public static final Comparator<FilterSpec> PRIORITY_COMPARATOR = new Comparator<FilterSpec>() {
        @Override
        public int compare(FilterSpec o1, FilterSpec o2) {
            return o1.getPriority() - o2.getPriority();
        }
    };

    private final Provider<? extends Filter> filterProvider;
    private final int priority;
    private final String name;

    public FilterSpec(Provider<? extends Filter> filterProvider, int priority, String name) {
        this.filterProvider = filterProvider;
        this.priority = priority;
        this.name = name;
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
