package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;

import io.dropwizard.metrics.graphite.GraphiteReporterFactory;

@JsonTypeName("graphite-hostaware")
public class HostAwareGraphiteReporterFactory extends GraphiteReporterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostAwareGraphiteReporterFactory.class);
    private static final CharMatcher NON_ALPHANUMERIC_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.negate();
    private static final String NON_ALPHANUMERIC_REPLACEMENT = "_";
    private static final String HOSTNAME_VARIABLE = "%s";
    private static final String INVALID_VARIABLE_PATTERN = ".*%[^s].*";
    private static final String UNKNOWN_HOSTNAME = "";

    private final String hostName;

    public HostAwareGraphiteReporterFactory() {
        this.hostName = getHostName();
    }

    private String getHostName() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String possibleHostName = address.getHostName();
            possibleHostName = NON_ALPHANUMERIC_MATCHER.replaceFrom(possibleHostName, NON_ALPHANUMERIC_REPLACEMENT);

            LOGGER.info("Using the following hostname for the graphite prefix: {}", possibleHostName);
            return possibleHostName;

        } catch (UnknownHostException e) {
            LOGGER.error("Could not get the local host name.", e);
            return UNKNOWN_HOSTNAME;
        }
    }

    @Override
    public void setPrefix(String prefix) {
        Preconditions.checkArgument(
                !prefix.matches(INVALID_VARIABLE_PATTERN),
                "The prefix contains an invalid hostname pattern.");

        super.setPrefix(prefix);
    }

    /**
     * Returns the prefix for the graphite reporter, formatting the prefix with the host name as input.
     * <p/>
     * An example of this: <pre>prefix = cluster.%s.mycomponent</pre>
     * <p/>
     * Will give: <pre>cluster.localhost.mycomponent</pre>
     * <p/>
     * Where localhost is the host name.
     * <p/>
     * It will also replace double dots {@code ..} with a single dot {@code .}.
     *
     * @return The prefix, with the host name appended.
     */
    @Override
    public String getPrefix() {
        String pattern = super.getPrefix();

        String prefix = pattern.replace(HOSTNAME_VARIABLE, hostName);
        prefix = prefix.replace("..", ".");

        return prefix;
    }
}
