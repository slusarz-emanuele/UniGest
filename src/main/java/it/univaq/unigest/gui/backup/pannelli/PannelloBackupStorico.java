package it.univaq.unigest.gui.backup.pannelli;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.util.backup.BackupManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.util.Arrays;

public class PannelloBackupStorico {

    private static final String BACKUP_DIR = BackupManager.BACKUP_PATH;

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titolo = new Label(Main.getParametrizzazioneHelper().getBundle().getString("titolo.pannelloBackup"));
        titolo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<File> listaBackup = new ListView<>();
        File dir = new File(BACKUP_DIR);
        File[] files = dir.exists() ? dir.listFiles((d, name) -> name.endsWith(".zip")) : new File[0];
        listaBackup.setItems(FXCollections.observableArrayList(files != null ? Arrays.asList(files) : Arrays.asList()));

        HBox pulsanti = new HBox(10);
        Button btnRipristina = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.ripristina.backup"));
        Button btnElimina = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.elimina.backup"));
        Button btnNuovo = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.nuovo.backup"));

        BackupManager manager = new BackupManager();

        btnNuovo.setOnAction(e -> {
            try {
                if (manager.creaBackup()){
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService"), Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService.sucess"));
                }
                aggiornaLista(listaBackup);
            } catch (Exception ex) {
                Dialogs.showError(Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService"), Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService.error") + ex.getMessage());
            }
        });

        btnElimina.setOnAction(e -> {
            File selezionato = listaBackup.getSelectionModel().getSelectedItem();
            if (selezionato != null && selezionato.delete()) {
                aggiornaLista(listaBackup);
            }
        });

        btnRipristina.setOnAction(e -> {
            File selezionato = listaBackup.getSelectionModel().getSelectedItem();
            if (selezionato != null) {
                if (manager.ripristinaBackup(selezionato)){
                    new Alert(Alert.AlertType.INFORMATION, "Backup" + selezionato.getName() + Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService.worked")).showAndWait();
                    new Alert(Alert.AlertType.INFORMATION, Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService.restart")).showAndWait();
                    Main.restartApp();
                }else{
                    new Alert(Alert.AlertType.ERROR,"Backup " + selezionato.getName() + Main.getParametrizzazioneHelper().getBundle().getString("alert.backupService.fail")).showAndWait();
                }
            }
        });

        pulsanti.getChildren().addAll(btnNuovo, btnRipristina, btnElimina);

        // === BACKUP AUTOMATICO ===
        HBox backupAuto = new HBox(10);
        Label lblAuto = new Label(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.backupService.cadenza"));
        ComboBox<String> giorniCombo = new ComboBox<>();
        giorniCombo.getItems().addAll("1", "3", "7", "15", "30", Main.getParametrizzazioneHelper().getBundle().getString("etichetta.backupService.mai"));
        giorniCombo.setValue(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.backupService.mai")); // Da mettere quello che legge dal file

        backupAuto.getChildren().addAll(lblAuto, giorniCombo);

        root.getChildren().addAll(titolo, listaBackup, pulsanti, new Separator(), backupAuto);
        return root;
    }

    private void aggiornaLista(ListView<File> lista) {
        File dir = new File(BACKUP_DIR);
        File[] files = dir.exists() ? dir.listFiles((d, name) -> name.endsWith(".zip")) : new File[0];
        lista.setItems(FXCollections.observableArrayList(files != null ? Arrays.asList(files) : Arrays.asList()));
    }
}
