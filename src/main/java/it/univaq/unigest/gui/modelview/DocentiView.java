package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello2;
import it.univaq.unigest.service.DocenteService;
import javafx.scene.layout.VBox;

public class DocentiView extends AbstractModelView<DocentiPannello2> {

    public DocentiView(DocenteService docenteService) {
        this.panel = new DocentiPannello2(docenteService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}
