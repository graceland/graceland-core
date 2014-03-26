package io.graceland.metrics.graphite;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ServiceLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.dropwizard.metrics.ReporterFactory;

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

    @Test
    public void prefix_replaces_non_alphanumerics_with_underscores() throws UnknownHostException {
        String hostName = "my.host.name-is_not a$number%123*2321";
        String expectedHostName = "my_host_name_is_not_a_number_123_2321";

        HostAwareGraphiteReporterFactory reporterFactory = buildReporter(hostName);

        reporterFactory.setPrefix("%s");
        assertThat(reporterFactory.getPrefix(), is(expectedHostName));
    }

    @Test
    public void remove_double_dots() throws UnknownHostException {
        HostAwareGraphiteReporterFactory reporterFactory = buildReporter("");

        reporterFactory.setPrefix("before.%s.after..last");
        assertThat(reporterFactory.getPrefix(), is("before.after.last"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void handles_poorly_formated_prefixes() throws UnknownHostException {
        HostAwareGraphiteReporterFactory reporterFactory = buildReporter("hostName");

        reporterFactory.setPrefix("before.%s.aft%er..last");
    }

    @Test
    public void replaces_more_than_once() throws UnknownHostException {
        HostAwareGraphiteReporterFactory reporterFactory = buildReporter("host");

        reporterFactory.setPrefix("before.%s.after.%s.last");
        assertThat(reporterFactory.getPrefix(), is("before.host.after.host.last"));
    }

    @Test
    public void loaded_by_service_provider() {
        boolean found = false;
        for (ReporterFactory reporterFactory : ServiceLoader.load(ReporterFactory.class)) {
            if (reporterFactory instanceof HostAwareGraphiteReporterFactory) {
                found = true;
            }
        }

        assertThat(found, is(true));
    }
}
