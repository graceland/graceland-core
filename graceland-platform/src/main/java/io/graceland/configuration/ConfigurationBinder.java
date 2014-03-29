package io.graceland.configuration;

import java.lang.annotation.Annotation;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;

public class ConfigurationBinder<T extends Configuration> {

    private final Class<T> klass;
    private final Binder binder;
    private Class<? extends Annotation> annotation = null;

    ConfigurationBinder(Class<T> klass, Binder binder) {
        this.klass = Preconditions.checkNotNull(klass, "Configuration Class cannot be null.");
        this.binder = Preconditions.checkNotNull(binder, "Binder cannot be null.");
    }

    public static <T extends Configuration> ConfigurationBinder<T> forClass(Class<T> klass, Binder binder) {
        return new ConfigurationBinder<T>(klass, binder);
    }

    public void toInstance(T configuration) {
        Preconditions.checkNotNull(configuration, "Configuration Instance cannot be null.");

        if (annotation == null) {
            binder.bind(klass).toInstance(configuration);

        } else {
            binder.bind(klass).annotatedWith(annotation).toInstance(configuration);
        }
    }

    public ConfigurationBinder<T> annotatedWith(Class<? extends Annotation> annotation) {
        this.annotation = Preconditions.checkNotNull(annotation, "Annotation cannot be null.");
        return this;
    }
}
