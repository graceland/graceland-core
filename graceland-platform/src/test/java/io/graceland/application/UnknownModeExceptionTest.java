package io.graceland.application;

import org.junit.Test;

import io.graceland.testing.TestModes;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class UnknownModeExceptionTest {

    private String candidate = "not-in-the-enum";
    private UnknownModeException exception = new UnknownModeException(TestModes.class, candidate);

    @Test
    public void message_contains_candidate() {
        assertThat(exception.getMessage(), containsString(candidate));
    }

    @Test
    public void message_lists_possible_enums() {
        for (TestModes mode : TestModes.values()) {
            assertThat(exception.getMessage(), containsString(mode.toString()));
        }
    }
}
