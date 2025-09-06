package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.CrudView;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class AbstractModelView<T extends CrudPanel> implements CrudView {

    protected T panel;

    public VBox getView(){
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

        // TabPane
        tabPane.getTabs().addAll(tabGestione);
        tabPane.getSelectionModel().select(tabGestione);
        tabPane.setStyle("-fx-background-color: #ffffff;");

        // Root
        root.getChildren().add(tabPane);
        return root;
    }

    public T getPannello (){
        return this.panel;
    }

    @Override public void onAdd()    { panel.apriDialogAggiungiPubblico(); }
    @Override public void onEdit()   { panel.modificaSelezionato(); }
    @Override public void onDelete() { panel.eliminaSelezionato(); }
    protected abstract VBox creaGestioneContenuto();

}
