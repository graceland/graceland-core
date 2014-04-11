package io.graceland.filter;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import com.google.common.collect.ImmutableList;

public class FilterPattern {
    public FilterPattern(EnumSet<DispatcherType> dispatcherTypes, boolean matchAfter, ImmutableList<String> urlPatterns) {
    }
}
