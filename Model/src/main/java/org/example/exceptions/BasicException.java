package org.example.exceptions;

import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public class BasicException extends Exception {
    protected final String messageKey;
    protected final Locale locale;

    public BasicException(String messageKey, Throwable cause) {
        super(getLocalizedMessage(messageKey, Locale.getDefault()), cause);
        this.messageKey = messageKey;
        this.locale = Locale.getDefault();
    }

    public BasicException(String messageKey) {
        super(getLocalizedMessage(messageKey, Locale.getDefault()));
        this.messageKey = messageKey;
        this.locale = Locale.getDefault();
    }

    public void log(Logger logger) {
        logger.error(getLocalizedMessage(messageKey, locale));
    }

    private static String getLocalizedMessage(String messageKey, Locale locale) {
        return ResourceBundle.getBundle("exceptions", locale).getString(messageKey);
    }
}
