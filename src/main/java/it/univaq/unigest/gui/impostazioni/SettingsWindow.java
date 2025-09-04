package it.univaq.unigest.gui.impostazioni;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.model.Settings;
import it.univaq.unigest.service.MaintenanceService;
import it.univaq.unigest.service.SettingsService;
import it.univaq.unigest.util.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class SettingsWindow {

    private final Stage stage;
    private final BorderPane root;
    private final SettingsService settingsService;
    private final MaintenanceService maintenanceService;

    private SettingsWindow(SettingsService settingsService, MaintenanceService maintenanceService) {
        this.settingsService = Objects.requireNonNull(settingsService);
        this.maintenanceService = Objects.requireNonNull(maintenanceService);

        this.stage = new Stage();
        this.root = new BorderPane();

        stage.setTitle("Impostazioni");
        stage.initModality(Modality.NONE); // finestra indipendente
        stage.setScene(new Scene(root, 900, 560));

        buildUI();
    }

    public static void open(SettingsService settingsService, MaintenanceService maintenanceService) {
        new SettingsWindow(settingsService, maintenanceService).stage.show();
    }

    // ===================== UI =====================
    private void buildUI() {
        // sidebar (sinistra)
        VBox side = new VBox(8);
        side.setPadding(new Insets(12));
        side.setPrefWidth(220);
        side.setStyle("-fx-background-color: #f2f4f8;");

        ToggleGroup group = new ToggleGroup();
        ToggleButton btnBackup     = makeNavButton("Backup", group);
        ToggleButton btnRipristino = makeNavButton("Ripristina / Reset", group);
        ToggleButton btnInfo       = makeNavButton("Informazioni", group);

        side.getChildren().addAll(btnBackup, btnRipristino, btnInfo);
        root.setLeft(side);

        // contenuto (destra)
        StackPane content = new StackPane();
        content.setPadding(new Insets(16));
        root.setCenter(content);

        // pannelli
        VBox backupPane     = buildBackupPane();
        VBox ripristinoPane = buildRipristinoPane();
        VBox infoPane       = buildInfoPane();

        content.getChildren().setAll(backupPane); // default
        btnBackup.setSelected(true);

        btnBackup.setOnAction(e -> content.getChildren().setAll(backupPane));
        btnRipristino.setOnAction(e -> content.getChildren().setAll(ripristinoPane));
        btnInfo.setOnAction(e -> content.getChildren().setAll(infoPane));
    }

    private ToggleButton makeNavButton(String text, ToggleGroup group) {
        ToggleButton tb = new ToggleButton(text);
        tb.setMaxWidth(Double.MAX_VALUE);
        tb.setToggleGroup(group);
        tb.getStyleClass().add("nav-toggle");
        tb.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #d9dfe8;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 10 12;
                -fx-font-size: 13px;
                """);
        tb.selectedProperty().addListener((obs, oldV, sel) -> {
            if (sel) tb.setStyle("""
                    -fx-background-color: #e8eefc;
                    -fx-border-color: #5b8def;
                    -fx-border-radius: 8;
                    -fx-background-radius: 8;
                    -fx-padding: 10 12;
                    -fx-font-size: 13px;
                    """);
            else tb.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: #d9dfe8;
                    -fx-border-radius: 8;
                    -fx-background-radius: 8;
                    -fx-padding: 10 12;
                    -fx-font-size: 13px;
                    """);
        });
        return tb;
    }

    // ===================== Pannelli =====================

    /** Pannello 1: Backup manuale + cartella backup nelle impostazioni */
    private VBox buildBackupPane() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(8));

        Label title = new Label("Backup");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Riga cartella backup (settings)
        HBox rigaFolder = new HBox(10);
        rigaFolder.setAlignment(Pos.CENTER_LEFT);

        Label lblFolder = new Label("Cartella backup (impostazioni):");
        TextField txtFolder = new TextField();
        txtFolder.setPrefWidth(420);

        // carica valore corrente dalle impostazioni
        Settings s = settingsService.get();
        txtFolder.setText(s != null && s.getCartellaBackup() != null ? s.getCartellaBackup() : "");

        Button btnScegli = new Button("Cambia…");
        btnScegli.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Scegli cartella backup");
            if (txtFolder.getText() != null && !txtFolder.getText().isBlank()) {
                File pre = new File(txtFolder.getText());
                if (pre.exists() && pre.isDirectory()) dc.setInitialDirectory(pre);
            }
            File dir = dc.showDialog(stage);
            if (dir != null) {
                txtFolder.setText(dir.getAbsolutePath());
                Settings cur = settingsService.get();
                if (cur == null) cur = Settings.defaults();
                cur.setCartellaBackup(dir.getAbsolutePath());
                //settingsService.save(cur);
                info("Impostazioni", "Cartella backup salvata.");
            }
        });

        Button btnApriCartella = new Button("Apri cartella dati…");
        btnApriCartella.setOnAction(e -> openInExplorer(new File(DatabaseHelper.PERCORSO_CARTELLA_DATI)));

        rigaFolder.getChildren().addAll(lblFolder, txtFolder, btnScegli, btnApriCartella);

        // Azione di backup
        Label hint = new Label("Il backup crea un file .zip dei file .json/.txt presenti nella cartella dati.");
        hint.setStyle("-fx-text-fill: #555;");

        Button btnBackup = new Button("Crea backup ora");
        btnBackup.setOnAction(e -> {
            boolean ok = maintenanceService.creaBackup();
            if (ok) info("Backup", "Backup creato correttamente nella cartella dati/backup.");
            else    errore("Backup", "Impossibile creare il backup.");
        });

        box.getChildren().addAll(title, rigaFolder, new Separator(), hint, btnBackup);
        return box;
    }

    /** Pannello 2: Ripristino da zip + resetta dati (wipe dei json/txt) */
    private VBox buildRipristinoPane() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(8));

        Label title = new Label("Ripristina / Reset");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblRestore = new Label("Ripristino da backup (.zip):");
        Button btnScegliZip = new Button("Scegli file .zip…");
        btnScegliZip.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleziona backup .zip");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup ZIP", "*.zip"));

            // cartella di default: prima "data/backup", poi "data", altrimenti home
            File dataDir   = new File(it.univaq.unigest.util.DatabaseHelper.PERCORSO_CARTELLA_DATI);
            File backupDir = new File(dataDir, "backup");
            if (backupDir.exists() && backupDir.isDirectory()) {
                fc.setInitialDirectory(backupDir);
            } else if (dataDir.exists() && dataDir.isDirectory()) {
                fc.setInitialDirectory(dataDir);
            } else {
                fc.setInitialDirectory(new File(System.getProperty("user.home")));
            }

            File zip = fc.showOpenDialog(stage);
            if (zip != null) {
                boolean ok = maintenanceService.ripristinaBackup(zip);
                if (ok) {
                    info("Ripristino", "Ripristino completato.\nL'app verrà riavviata.");
                    Main.restartApp();
                } else {
                    errore("Ripristino", "Impossibile ripristinare dal backup selezionato.");
                }
            }
        });


        Label lblReset = new Label("Reset (cancella i dati .json/.txt in cartella dati):");
        lblReset.setStyle("-fx-text-fill: #a00;");

        Button btnReset = new Button("Resetta dati");
        btnReset.setStyle("-fx-background-color: #ffdddd; -fx-border-color: #ffaaaa;");
        btnReset.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                    "Questa operazione cancellerà i file .json/.txt nella cartella dati (la cartella 'backup' NON verrà toccata).\nVuoi continuare?",
                    ButtonType.YES, ButtonType.NO);
            a.setHeaderText("Conferma reset dati");
            a.initOwner(stage);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    boolean ok = maintenanceService.resettaDati();
                    if (ok) {
                        info("Reset", "Reset completato.\nL'app verrà riavviata.");
                        Main.restartApp();
                    } else {
                        errore("Reset", "Impossibile completare il reset.");
                    }
                }
            });
        });

        box.getChildren().addAll(title,
                lblRestore, btnScegliZip,
                new Separator(),
                lblReset, btnReset
        );
        return box;
    }

    /** Pannello 3: Informazioni / utilità */
    private VBox buildInfoPane() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(8));

        Label title = new Label("Informazioni");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label info = new Label("""
                UniGest — pannello Impostazioni
                • Backup: crea uno ZIP dei file .json/.txt in cartella dati/backup.
                • Ripristino: sostituisce i file .json/.txt con quelli contenuti nello ZIP.
                • Reset: cancella i file .json/.txt dalla cartella dati (non tocca la cartella 'backup').

                Suggerimento: esegui periodicamente un backup manuale.
                """);
        info.setStyle("-fx-text-fill: #444;");

        Button btnApriDati = new Button("Apri cartella dati…");
        btnApriDati.setOnAction(e -> openInExplorer(new File(DatabaseHelper.PERCORSO_CARTELLA_DATI)));

        box.getChildren().addAll(title, info, btnApriDati);
        return box;
    }

    // ===================== Helpers =====================
    private void info(String titolo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(titolo);
        a.initOwner(stage);
        a.showAndWait();
    }

    private void errore(String titolo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(titolo);
        a.initOwner(stage);
        a.showAndWait();
    }

    private void openInExplorer(File dir) {
        try {
            if (dir != null && dir.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(dir);
                } else {
                    info("Apri cartella", "Desktop non supportato su questo sistema.");
                }
            } else {
                errore("Apri cartella", "Cartella inesistente:\n" + dir);
            }
        } catch (IOException ex) {
            errore("Apri cartella", "Impossibile aprire la cartella:\n" + ex.getMessage());
        }
    }
}