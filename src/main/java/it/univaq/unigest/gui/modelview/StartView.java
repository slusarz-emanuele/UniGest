package it.univaq.unigest.gui.modelview;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartView {

    public void start(Stage stagePrimario) {
        BorderPane root = new BorderPane();

        Label label = new Label("Benvenuto in UniGest!");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");

        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        // Crea scena
        Scene scene = new Scene(root, 800, 600);

        stagePrimario.setScene(scene);
        stagePrimario.setTitle("UniGest — ");
        stagePrimario.show();
    }
}
