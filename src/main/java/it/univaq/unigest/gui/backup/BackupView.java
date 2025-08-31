package it.univaq.unigest.gui.backup;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.backup.pannelli.PannelloBackupStorico;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BackupView {

    private Button bottoneAttivo = null;

    public Stage getStage(Stage stagePrimario) {
        Stage backupStage = new Stage();
        backupStage.initOwner(stagePrimario);
        backupStage.initModality(Modality.APPLICATION_MODAL);
        backupStage.setTitle("Backup");

        BorderPane root = new BorderPane();

        // --- SIDEBAR SINISTRA ---
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #34495E;");
        sidebar.setPrefWidth(200);

        Button storicoBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.backup.storico"));

        stileMenu(storicoBtn);

        sidebar.getChildren().addAll(
                storicoBtn
        );

        // --- CONTENUTO CENTRALE ---
        VBox contentArea = new VBox(10);
        contentArea.setAlignment(Pos.TOP_LEFT);
        contentArea.setPadding(new Insets(20));
        contentArea.getChildren().add(new Label(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.backup.azione")));

        storicoBtn.setOnAction(e -> {
            handleButtonClick(storicoBtn);
            PannelloBackupStorico pannello = new PannelloBackupStorico();
            contentArea.getChildren().setAll(pannello.getView());
        });


        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 600, 400);
        backupStage.setScene(scene);
        return backupStage;
    }

    private void stileMenu(Button btn) {
        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
            -fx-padding: 6 10 6 10;
        """);

        btn.setOnMouseEntered(e -> {
            if (btn != bottoneAttivo) {
                btn.setStyle("""
                    -fx-background-color: #3E5871;
                    -fx-text-fill: white;
                    -fx-font-size: 14px;
                    -fx-alignment: CENTER_LEFT;
                    -fx-padding: 6 10 6 10;
                """);
            }
        });

        btn.setOnMouseExited(e -> {
            if (btn != bottoneAttivo) {
                btn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-text-fill: white;
                    -fx-font-size: 14px;
                    -fx-alignment: CENTER_LEFT;
                    -fx-padding: 6 10 6 10;
                """);
            }
        });
    }

    private void handleButtonClick(Button clickedBtn) {
        if (bottoneAttivo != null) {
            stileMenu(bottoneAttivo);
        }
        clickedBtn.setStyle("""
            -fx-background-color: #1ABC9C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
            -fx-padding: 6 10 6 10;
        """);
        bottoneAttivo = clickedBtn;
    }

    private Separator creaSeparatoreMenu() {
        Separator separatore = new Separator();
        separatore.setPrefWidth(180);
        separatore.setStyle("""
            -fx-background-color: transparent;
            -fx-border-style: solid;
            -fx-border-color: rgba(255,255,255,0.2);
            -fx-border-width: 0 0 1 0;
            -fx-padding: 8 0 8 0;
        """);
        return separatore;
    }
}
