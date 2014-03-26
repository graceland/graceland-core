package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.CharMatcher;

import io.dropwizard.metrics.graphite.GraphiteReporterFactory;

public class HostAwareGraphiteReporterFactory extends GraphiteReporterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostAwareGraphiteReporterFactory.class);
    private static final CharMatcher NON_ALPHANUMERIC = CharMatcher.JAVA_LETTER_OR_DIGIT.negate();
    private static final String HOSTNAME_VARIABLE = "%s";
    private static final String HOSTNAME_REGEX = ".*%[^s].*";

    private final String hostName;

    public HostAwareGraphiteReporterFactory() {
        String possibleHostName = "";

        try {
            InetAddress address = InetAddress.getLocalHost();
            possibleHostName = address.getHostName();
            possibleHostName = NON_ALPHANUMERIC.replaceFrom(possibleHostName, "_");
            LOGGER.info("Using the following hostname for the graphite prefix: {}", possibleHostName);

        } catch (UnknownHostException e) {
            LOGGER.error("Could not get the local host name.", e);
        }

        this.hostName = possibleHostName;
    }

    @Override
    public void setPrefix(String prefix) {
        String regex = HOSTNAME_REGEX;

        if (prefix.matches(regex)) {
            throw new IllegalArgumentException("The prefix can not contain a `%` without a trailing `s`.");
        }

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
