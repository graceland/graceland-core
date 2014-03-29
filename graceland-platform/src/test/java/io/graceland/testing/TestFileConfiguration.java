package io.graceland.testing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.graceland.configuration.Configuration;

public class TestFileConfiguration implements Configuration {

    private final int abc;
    private final String test;

    @JsonCreator
    public TestFileConfiguration(
            @JsonProperty("abc") int abc,
            @JsonProperty("test") String test) {

        this.abc = abc;
        this.test = test;
    }

    public int getAbc() {
        return abc;
    }

    public String getTest() {
        return test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestFileConfiguration that = (TestFileConfiguration) o;

        if (abc != that.abc) return false;
        if (test != null ? !test.equals(that.test) : that.test != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = abc;
        result = 31 * result + (test != null ? test.hashCode() : 0);
        return result;
    }
}
