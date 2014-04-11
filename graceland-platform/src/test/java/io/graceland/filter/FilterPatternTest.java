package io.graceland.filter;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FilterPatternTest {

    private static final EnumSet<DispatcherType> VALID_DISPATCHER_TYPES = EnumSet.allOf(DispatcherType.class);
    private static final boolean VALID_MATCH_AFTER = true;
    private static final ImmutableList<String> VALID_URL_PATTERNS = ImmutableList.of("/*", "/test/*");

    @Test(expected = NullPointerException.class)
    public void must_have_dispatcherType() {
        new FilterPattern(null, VALID_MATCH_AFTER, VALID_URL_PATTERNS);
    }

    @Test(expected = NullPointerException.class)
    public void must_have_urlPatterns() {
        new FilterPattern(VALID_DISPATCHER_TYPES, VALID_MATCH_AFTER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dispatcherTypes_cannot_be_empty() {
        new FilterPattern(EnumSet.noneOf(DispatcherType.class), VALID_MATCH_AFTER, VALID_URL_PATTERNS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void urlPatterns_cannot_be_empty() {
        new FilterPattern(VALID_DISPATCHER_TYPES, VALID_MATCH_AFTER, ImmutableList.<String>of());
    }

    @Test
    public void helper_builder_with_vararg_patterns() {
        FilterPattern pattern = FilterPattern.forPatterns("/*", "other");

        assertThat(pattern.getDispatcherTypes(), is(FilterPattern.DEFAULT_DISPATCHER_TYPES));
        assertThat(pattern.isMatchAfter(), is(FilterPattern.DEFAULT_MATCH_AFTER));
        assertThat(pattern.getUrlPatterns(), containsInAnyOrder("/*", "other"));
    }

    @Test
    public void helper_builder_with_iterable_patterns() {
        Iterable<String> iterable = ImmutableList.of("/*", "other");
        FilterPattern pattern = FilterPattern.forPatterns(iterable);

        assertThat(pattern.getDispatcherTypes(), is(FilterPattern.DEFAULT_DISPATCHER_TYPES));
        assertThat(pattern.isMatchAfter(), is(FilterPattern.DEFAULT_MATCH_AFTER));
        assertThat(pattern.getUrlPatterns(), containsInAnyOrder("/*", "other"));
    }

    @Test
    public void helper_builder_with_all_three_iterable() {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.ASYNC);
        Iterable<String> patterns = ImmutableList.of("/*", "other");
        FilterPattern pattern = FilterPattern.newInstance(dispatcherTypes, true, patterns);

        assertThat(pattern.getDispatcherTypes(), is(dispatcherTypes));
        assertThat(pattern.isMatchAfter(), is(true));
        assertThat(pattern.getUrlPatterns(), containsInAnyOrder("/*", "other"));
    }

    @Test
    public void helper_builder_with_all_three_varargs() {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.ASYNC);
        FilterPattern pattern = FilterPattern.newInstance(dispatcherTypes, true, "/*", "other");

        assertThat(pattern.getDispatcherTypes(), is(dispatcherTypes));
        assertThat(pattern.isMatchAfter(), is(true));
        assertThat(pattern.getUrlPatterns(), containsInAnyOrder("/*", "other"));
    }
}
