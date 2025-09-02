package it.univaq.unigest.gui.util;

import javafx.scene.layout.VBox;

public interface CrudPanel {
    VBox getView();

    void apriDialogAggiungiPubblico();
    void modificaSelezionato();
    void eliminaSelezionato();

    void refresh();
}
