package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello1;
import it.univaq.unigest.service.EdificioService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

public class EdificioView extends AbstractModelView<EdificiPannello1> {

    public EdificioView(EdificioService edificioService,
                        DomainQueryService domainQueryService){
        this.panel = new EdificiPannello1(edificioService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
