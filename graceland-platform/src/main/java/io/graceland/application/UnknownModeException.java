package io.graceland.application;

/**
 * An exception that is thrown when an unknown mode is passed to a {@link io.graceland.application.ModalApplication}
 * through the command line arguments.
 */
public class UnknownModeException extends RuntimeException {

    private final Class modeClass;
    private final String candidate;

    UnknownModeException(
            Class modeClass,
            String candidate) {

        this.modeClass = modeClass;
        this.candidate = candidate;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();

        sb
                .append("An unknown mode was found in the command line arguments: `")
                .append(candidate)
                .append("`.\n\n")
                .append("The possible choices are (they are case-sensitive):\n");

        for (Object e : modeClass.getEnumConstants()) {
            sb
                    .append("\t- ")
                    .append(e)
                    .append("\n");
        }

        return sb.toString();
    }
}
