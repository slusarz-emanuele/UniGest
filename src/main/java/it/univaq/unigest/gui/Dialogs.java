package it.univaq.unigest.gui;

import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class Dialogs {

    private static void showAlert(String titolo, String messaggio, AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.getDialogPane().getStylesheets().add(getCss());
        alert.setTitle(titolo);
        if(alertType == AlertType.INFORMATION){
            alert.setHeaderText(null);
        }else{
            alert.setHeaderText(Main.getParametrizzazioneHelper().getBundle().getString("alert.header.error"));
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
    
    @SuppressWarnings("DataFlowIssue")
    private static String getCss() {
        try {
            return Dialogs.class.getResource("/css/dialogs.css").toExternalForm();
        } catch (NullPointerException e) {
            LogHelper.saveLog(LogType.ERROR, "File CSS non trovato: /css/dialogs.css");
            return "";
        }
    }


}
