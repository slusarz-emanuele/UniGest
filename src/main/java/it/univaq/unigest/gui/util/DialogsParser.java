package it.univaq.unigest.gui.util;

import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.util.Map;

public class DialogsParser {

    public static String validaCampo(Map<String, Control> campi, String chiave) throws CampoRichiestoVuoto {
        Control control = campi.get(chiave);

        if (!(control instanceof TextField)) {
            throw new CampoRichiestoVuoto("Il campo '" + chiave + "' non è un TextField valido");
        }

        TextField field = (TextField) control;

        if (field.getText() == null || field.getText().trim().isEmpty()) {
            throw new CampoRichiestoVuoto(chiave + " non inserito");
        }

        return field.getText().trim();
    }

}
