package io.graceland.filter;

import java.util.Comparator;
import javax.servlet.Filter;

public class FilterSpec {
    public static final int DEFAULT_PRIORITY = 500;
    public static final Comparator<FilterSpec> PRIORITY_COMPARATOR = new Comparator<FilterSpec>() {
        @Override
        public int compare(FilterSpec o1, FilterSpec o2) {
            return o1.getPriority() - o2.getPriority();
        }
    };

    private final Filter filter;
    private final int priority;
    private final String name;


    FilterSpec(Filter filter, int priority, String name) {
        this.filter = filter;
        this.priority = priority;
        this.name = name;
    }

    public static FilterSpecBuilder forFilter(Filter filter) {
        return new FilterSpecBuilder(filter);
    }

    public Filter getFilter() {
        return filter;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public static class FilterSpecBuilder {
        private final Filter filter;
        private int priority = DEFAULT_PRIORITY;
        private String name = null;

        public FilterSpecBuilder(Filter filter) {
            this.filter = filter;
        }

        public FilterSpecBuilder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public FilterSpecBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FilterSpec build() {
            if (name == null) {
                name = filter.getClass().getSimpleName();
            }

            return new FilterSpec(filter, priority, name);
        }
    }
}
