package io.graceland.plugin.loaders;

import java.util.ServiceLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.google.common.collect.ImmutableList;

import io.graceland.plugin.Plugin;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NativePluginLoader.class)
public class NativePluginLoaderTest extends PluginLoaderTest<NativePluginLoader> {

    @Override
    protected NativePluginLoader newPluginLoader() {
        return NativePluginLoader.newLoader();
    }

    @Test
    public void gets_plugins_from_serviceloader() {
        Plugin plugin1 = mock(Plugin.class);
        Plugin plugin2 = mock(Plugin.class);
        Plugin plugin3 = mock(Plugin.class);

        ServiceLoader dummyLoader = PowerMockito.mock(ServiceLoader.class);
        when(dummyLoader.iterator()).thenReturn(ImmutableList.of(plugin1, plugin2, plugin3).iterator());

        PowerMockito.mockStatic(ServiceLoader.class);
        when(ServiceLoader.load(any(Class.class))).thenReturn(dummyLoader);

        pluginLoader.loadInto(application);

        verify(application).loadPlugin(eq(plugin1));
        verify(application).loadPlugin(eq(plugin2));
        verify(application).loadPlugin(eq(plugin3));
    }
}
