package it.univaq.unigest.gui;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dialogs {

    private static final Logger LOGGER = LogManager.getLogger(Dialogs.class);

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

    public static void showInfo(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.INFORMATION);
    }

    public static void showError(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.ERROR);
    }

    public static void showWarning(String titolo, String messaggio) {
        showAlert(titolo, messaggio, AlertType.WARNING);
    }
    
    @SuppressWarnings("DataFlowIssue")
    private static String getCss() {
        try {
            return Dialogs.class.getResource("/css/dialogs.css").toExternalForm();
        } catch (NullPointerException e) {
            LOGGER.error("File CSS non trovato: /css/dialogs.css");
            return "";
        }
    }

    public static boolean confirm(String title, String content) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        a.setTitle(title);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

}
