package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HostAwareGraphiteReporterFactoryTest {

    private HostAwareGraphiteReporterFactory reporterFactory = new HostAwareGraphiteReporterFactory();

    private String expectedHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    @Test
    public void prefix_replaces_host() throws UnknownHostException {
        String hostName = expectedHostName();

        String prefix = "test.%s.this.out";
        String expectedPrefix = "test." + hostName + ".this.out";

        String prefix_missing = "test.this.out";
        String expectedPrefix_missing = "test.this.out";

        String prefix_solo = "%s";
        String expectedPrefix_solo = hostName;

        reporterFactory.setPrefix(prefix);
        assertThat(reporterFactory.getPrefix(), is(expectedPrefix));

        reporterFactory.setPrefix(prefix_missing);
        assertThat(reporterFactory.getPrefix(), is(expectedPrefix_missing));

        reporterFactory.setPrefix(prefix_solo);
        assertThat(reporterFactory.getPrefix(), is(expectedPrefix_solo));
    }
}
