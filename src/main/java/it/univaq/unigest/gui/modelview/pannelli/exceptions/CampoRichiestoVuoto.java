package it.univaq.unigest.gui.modelview.pannelli.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CampoRichiestoVuoto extends RuntimeException {

    private static final Logger LOGGER = LogManager.getLogger(CampoRichiestoVuoto.class);

    public CampoRichiestoVuoto(String message) {
        super(message);
        LOGGER.error(message);
    }

}
