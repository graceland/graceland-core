package io.graceland.filter;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class FilterPattern {
    private final EnumSet<DispatcherType> dispatcherTypes;
    private final boolean matchAfter;
    private final ImmutableList<String> urlPatterns;

    public FilterPattern(EnumSet<DispatcherType> dispatcherTypes, boolean matchAfter, ImmutableList<String> urlPatterns) {
        Preconditions.checkNotNull(dispatcherTypes, "Dispatcher Types cannot be null.");
        Preconditions.checkNotNull(urlPatterns, "URL Patterns cannot be null.");

        Preconditions.checkArgument(!dispatcherTypes.isEmpty(), "Dispatcher Arguments cannot be empty");
        Preconditions.checkArgument(!urlPatterns.isEmpty(), "URL Patterns cannot be empty");

        this.dispatcherTypes = dispatcherTypes;
        this.matchAfter = matchAfter;
        this.urlPatterns = urlPatterns;
    }

    public EnumSet<DispatcherType> getDispatcherTypes() {
        return dispatcherTypes;
    }

    public boolean isMatchAfter() {
        return matchAfter;
    }

    public ImmutableList<String> getUrlPatterns() {
        return urlPatterns;
    }
}
