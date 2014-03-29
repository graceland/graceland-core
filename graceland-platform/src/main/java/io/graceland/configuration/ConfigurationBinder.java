package io.graceland.configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import javax.validation.Validation;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;

import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;

public class ConfigurationBinder<Config extends Configuration> {

    private final Class<Config> configClass;
    private final Binder binder;
    private Class<? extends Annotation> annotationClass = null;

    ConfigurationBinder(Class<Config> configClass, Binder binder) {
        this.configClass = Preconditions.checkNotNull(configClass, "Configuration Class cannot be null.");
        this.binder = Preconditions.checkNotNull(binder, "Binder cannot be null.");
    }

    public static <T extends Configuration> ConfigurationBinder<T> forClass(Class<T> klass, Binder binder) {
        return new ConfigurationBinder<>(klass, binder);
    }

    public ConfigurationBinder<Config> annotatedWith(Class<? extends Annotation> annotation) {
        this.annotationClass = Preconditions.checkNotNull(annotation, "Annotation cannot be null.");
        return this;
    }

    public void toInstance(Config configuration) {
        Preconditions.checkNotNull(configuration, "Configuration Instance cannot be null.");

        if (annotationClass == null) {
            binder.bind(configClass).toInstance(configuration);

        } else {
            binder.bind(configClass).annotatedWith(annotationClass).toInstance(configuration);
        }
    }

    public void toFile(String fileName) {
        Preconditions.checkNotNull(fileName, "File Name cannot be null.");

        Config configuration = buildFromFile(fileName);

        if (annotationClass == null) {
            binder.bind(configClass).toInstance(configuration);

        } else {
            binder.bind(configClass).annotatedWith(annotationClass).toInstance(configuration);
        }
    }

    /**
     * Builds a Configuration object from the file path given. It uses the {@link io.dropwizard.configuration.ConfigurationFactory}
     * to build the configuration.
     *
     * @param possibleFilename The path to the configuration.
     * @return A configuration object loaded form the filename given.
     */
    private Config buildFromFile(String possibleFilename) {
        File configFile = new File(possibleFilename);
        Preconditions.checkArgument(configFile.exists(), "File must exist at: " + configFile.getAbsolutePath());

        try {
            return new ConfigurationFactory<>(
                    configClass,
                    Validation.buildDefaultValidatorFactory().getValidator(),
                    Jackson.newObjectMapper(),
                    "graceland")
                    .build(configFile);

        } catch (Exception e) {
            String msg = "Unknown exception triggered when attempting to build config from file:" + "\n" +
                    "\t* Configuration Class: " + configClass.getCanonicalName() + "\n" +
                    "\t* File: " + configFile.getAbsolutePath();

            throw new RuntimeException(msg, e);
        }
    }
}
