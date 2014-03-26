package io.graceland.applications;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * An application that uses it's mode to determine what plugins to load.
 * <p/>
 * The mode is usually defined in the command line arguments, by adding a new argument like `--DEV` for dev mode.
 * <p/>
 * To use this class, extend it and provide the modeClass and defaultMode.
 *
 * @param <E> The different modes that this application will be aware of.
 */
public abstract class ModalApplication<E extends Enum<E>>
        extends SimpleApplication
        implements Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModalApplication.class);
    private final E mode;

    /**
     * @param modeClass   The mode enum class, because java has type erasure.
     * @param defaultMode The mode to use if none is found in the arguments.
     * @param args        The arguments to look through for the mode.
     */
    protected ModalApplication(
            Class<E> modeClass,
            E defaultMode,
            String[] args) {

        Preconditions.checkNotNull(modeClass, "Mode Class cannot be null.");
        Preconditions.checkNotNull(defaultMode, "Default Mode cannot be null.");
        Preconditions.checkNotNull(args, "Arguments cannot be null.");

        mode = determineMode(modeClass, args).or(defaultMode);
        LOGGER.info("Application Mode: {}", mode);
    }

    Optional<E> determineMode(Class<E> modeClass, String[] args) {
        for (String arg : args) {
            if (StringUtils.isNotEmpty(arg) && arg.startsWith("--")) {
                String candidate = arg.substring(2);

                try {
                    return Optional.of(Enum.valueOf(modeClass, candidate));

                } catch (Exception e) {
                    LOGGER.error("Unknown mode found when reading through arguments. Attempting to find mode for: {}", candidate, e);
                    throw new UnknownModeException(modeClass, candidate);
                }
            }
        }

        return Optional.absent();
    }

    protected abstract void configureFor(E mode);

    /**
     * Called when plugins are being determined. The {@link #configureFor(Enum)} method will be called with the proper
     * mode, allowing specific plugins to be loaded based on the mode.
     */
    @Override
    protected void configure() {
        configureFor(mode);
    }

    public E getMode() {
        return mode;
    }
}
