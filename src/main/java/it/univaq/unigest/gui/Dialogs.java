package it.univaq.unigest.gui;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility centralizzata per la gestione di finestre di dialogo JavaFX.
 * <p>
 * Fornisce metodi statici per mostrare messaggi informativi, di errore,
 * di avviso e conferme. Applica (se presente /css/dialogs.css) un foglio di stile dedicato
 * alle dialog per uniformare l'aspetto.
 *
 * <h2>Note</h2>
 * <ul>
 *   <li>I metodi bloccano l'esecuzione finché l'utente non chiude la finestra
 *       (uso di {@code showAndWait()}).</li>
 *   <li>Se il CSS {@code /css/dialogs.css} non è disponibile, il log riporterà un errore
 *       ma la dialog verrà comunque mostrata con lo stile di default.</li>
 * </ul>
 */
public class Dialogs {

    private static final Logger LOGGER = LogManager.getLogger(Dialogs.class);

    /**
     * Mostra una finestra di dialogo generica.
     *
     * @param titolo     titolo della finestra
     * @param messaggio  testo del messaggio
     * @param alertType  tipologia di alert (INFO, WARNING, ERROR, ecc.)
     */
    private static void showAlert(String titolo, String messaggio, AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.getDialogPane().getStylesheets().add(getCss());
        alert.setTitle(titolo);
        if(alertType == AlertType.INFORMATION){
            alert.setHeaderText(null);
        }else{
            alert.setHeaderText("Errore");
        }
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra una dialog informativa.
     *
     * @param titolo    titolo della finestra
     * @param messaggio testo del messaggio
     */
    public static void showInfo(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.INFORMATION);
    }

    /**
     * Mostra una dialog di errore.
     *
     * @param titolo    titolo della finestra
     * @param messaggio testo del messaggio
     */
    public static void showError(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.ERROR);
    }

    /**
     * Mostra una dialog di avviso (warning).
     *
     * @param titolo    titolo della finestra
     * @param messaggio testo del messaggio
     */
    public static void showWarning(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.WARNING);
    }

    /**
     * Restituisce l'URL del CSS per le dialog, se disponibile.
     * <p>
     * In caso di assenza del file o percorso errato, logga un errore e
     * restituisce stringa vuota, consentendo comunque la visualizzazione
     * delle dialog con stile di default.
     *
     * @return URL del CSS da applicare alle dialog, oppure stringa vuota
     */
    @SuppressWarnings("DataFlowIssue")
    private static String getCss() {
        try {
            return Dialogs.class.getResource("/css/dialogs.css").toExternalForm();
        } catch (NullPointerException e) {
            LOGGER.error("File CSS non trovato: /css/dialogs.css");
            return "";
        }
    }

    /**
     * Mostra una dialog di conferma con bottoni OK/Cancel.
     *
     * @param title   titolo della finestra
     * @param content testo del messaggio
     * @return {@code true} se l'utente conferma (OK), {@code false} altrimenti
     */
    public static boolean confirm(String title, String content) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        a.setTitle(title);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

}
