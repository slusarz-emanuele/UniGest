package it.univaq.unigest.gui.util;

import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

import java.util.Map;

/**
 * Utility per l’estrazione e la validazione di campi testuali
 * raccolti in una mappa di controlli JavaFX (nei dialog CRUD).
 * <p>
 * Al momento gestisce esclusivamente campi di tipo {@link TextField}:
 * verifica che il controllo esista, che sia effettivamente un TextField
 * e che contenga un valore non vuoto. In caso contrario solleva
 * l’eccezione applicativa {@link CampoRichiestoVuoto}.
 * <p>
 * Uso tipico:
 * <pre>{@code
 * String cf = DialogsParser.validaCampo(campi, "CF");
 * }</pre>
 */
public class DialogsParser {

    /**
     * Estrae e valida il contenuto di un {@link TextField} dalla mappa dei controlli.
     *
     * @param campi  mappa "etichetta → controllo" usata per costruire il dialog
     * @param chiave etichetta/chiave con cui è stato registrato il campo
     * @return il testo del campo ripulito (trim) se presente e non vuoto
     * @throws CampoRichiestoVuoto se:
     *                             <ul>
     *                               <li>il controllo mancante o non è un {@code TextField};</li>
     *                               <li>il valore è nullo o composto solo da spazi.</li>
     *                             </ul>
     */
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
