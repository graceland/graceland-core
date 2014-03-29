package io.graceland.configuration;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;

public class ConfigurationBinder<T extends Configuration> {

    private Class<T> klass;
    private final Binder binder;

    ConfigurationBinder(Class<T> klass, Binder binder) {
        this.klass = Preconditions.checkNotNull(klass, "Configuration Class cannot be null.");
        this.binder = Preconditions.checkNotNull(binder, "Binder cannot be null.");
    }

    public static <T extends Configuration> ConfigurationBinder<T> forClass(Class<T> klass, Binder binder) {
        return new ConfigurationBinder<T>(klass, binder);
    }

    public void toInstance(T configuration) {
        binder.bind(klass).toInstance(configuration);
    }
}
