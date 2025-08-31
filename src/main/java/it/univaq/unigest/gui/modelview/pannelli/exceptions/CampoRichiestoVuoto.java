package it.univaq.unigest.gui.modelview.pannelli.exceptions;

import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

public class CampoRichiestoVuoto extends RuntimeException {

    public CampoRichiestoVuoto(String message) {
        super(message);
        LogHelper.saveLog(LogType.ERROR, message);
    }

}
