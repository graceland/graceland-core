package io.graceland.filter;

import javax.servlet.Filter;

public class FilterSpec {
    private final Filter filter;
    private final int prioirty;


    public FilterSpec(Filter filter, int prioirty) {
        this.filter = filter;
        this.prioirty = prioirty;
    }

    public Filter getFilter() {
        return filter;
    }

    public int getPrioirty() {
        return prioirty;
    }
}
