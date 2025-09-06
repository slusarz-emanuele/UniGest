package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello1;
import it.univaq.unigest.service.DocenteService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

public class DocentiView extends AbstractModelView<DocentiPannello1> {

    public DocentiView(DocenteService docenteService,
                       DomainQueryService domainQueryService) {
        this.panel = new DocentiPannello1(docenteService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
