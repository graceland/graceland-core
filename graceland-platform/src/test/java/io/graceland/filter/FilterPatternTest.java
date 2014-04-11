package io.graceland.filter;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import org.junit.Test;
import com.google.common.collect.ImmutableList;

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
}
