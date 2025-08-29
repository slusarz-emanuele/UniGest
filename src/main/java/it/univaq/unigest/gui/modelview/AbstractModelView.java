package it.univaq.unigest.gui.modelview;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class AbstractModelView {

    protected VBox getView(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TabPane tabPane = new TabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Tab 1
        Tab tabGestione = new Tab("Gestione");
        tabGestione.setClosable(false);

        VBox layoutGestione = creaGestioneContenuto();
        tabGestione.setContent(layoutGestione);

        VBox.setVgrow(layoutGestione, Priority.ALWAYS);

        // Tab 2
        Tab tabStatistiche = new Tab("Statistiche");
        tabStatistiche.setClosable(false);

        VBox layoutStatistiche = creaStatisticheContenuto();
        tabStatistiche.setContent(layoutStatistiche);

        // TabPane
        tabPane.getTabs().addAll(tabGestione, tabStatistiche);
        tabPane.getSelectionModel().select(tabGestione);
        tabPane.setStyle("-fx-background-color: #ffffff;");

        // Root
        root.getChildren().add(tabPane);
        return root;
    }

    protected abstract VBox creaGestioneContenuto();

    protected abstract VBox creaStatisticheContenuto();

}
