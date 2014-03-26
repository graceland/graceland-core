package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HostAwareGraphiteReporterFactory.class)
public class HostAwareGraphiteReporterFactoryTest {

    private HostAwareGraphiteReporterFactory buildReporter(String hostName) throws UnknownHostException {
        InetAddress localHost = mock(InetAddress.class);
        when(localHost.getHostName()).thenReturn(hostName);

        PowerMockito.mockStatic(InetAddress.class);
        when(InetAddress.getLocalHost()).thenReturn(localHost);

        return new HostAwareGraphiteReporterFactory();
    }

    @Test
    public void prefix_replaces_host() throws UnknownHostException {
        String hostName = "myhostname";
        HostAwareGraphiteReporterFactory reporterFactory = buildReporter(hostName);

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
