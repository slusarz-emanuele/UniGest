package it.univaq.unigest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private static final Logger LOGGER = LogManager.getLogger("it.univaq.unigest");

    public static void info(String messaggio) {
        LOGGER.info(messaggio);
    }

    public static void warn(String messaggio) {
        LOGGER.warn(messaggio);
    }

    public static void error(String messaggio) {
        LOGGER.error(messaggio);
    }

    public static void debug(String messaggio) {
        LOGGER.debug(messaggio);
    }
}
