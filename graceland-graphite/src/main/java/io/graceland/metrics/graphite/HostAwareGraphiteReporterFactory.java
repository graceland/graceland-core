package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.metrics.graphite.GraphiteReporterFactory;

public class HostAwareGraphiteReporterFactory extends GraphiteReporterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostAwareGraphiteReporterFactory.class);
    private final String hostName;

    public HostAwareGraphiteReporterFactory() {
        String possibleHostName = "";

        try {
            InetAddress address = InetAddress.getLocalHost();
            possibleHostName = address.getHostName();
            LOGGER.info("Using the following hostname for the graphite prefix: {}", possibleHostName);

        } catch (UnknownHostException e) {
            LOGGER.error("Could not get the local host name.", e);
        }

        this.hostName = possibleHostName;
    }

    /**
     * Returns the prefix for the graphite reporter, formatting the prefix with the host name as input.
     * <p/>
     * An example of this: <pre>prefix = cluster.%s.mycomponent</pre>
     * <p/>
     * Will give: <pre>cluster.localhost.mycomponent</pre>
     * <p/>
     * Where localhost is the host name.
     *
     * @return The prefix, with the host name appended.
     */
    @Override
    public String getPrefix() {
        String pattern = super.getPrefix();
        return String.format(pattern, hostName);
    }
}
