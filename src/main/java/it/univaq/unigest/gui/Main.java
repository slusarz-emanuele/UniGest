package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.StartView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stagePrimario;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stagePrimario = stage;

        new StartView().start(stagePrimario);
    }

    public static Stage getPrimaryStage() {
        return stagePrimario;
    }
}
