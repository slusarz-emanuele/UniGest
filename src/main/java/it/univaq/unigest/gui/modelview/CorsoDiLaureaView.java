package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

public class CorsoDiLaureaView extends AbstractModelView<CorsiDiLaureaPannello2> {

    public CorsoDiLaureaView(CorsoDiLaureaService corsiDiLaureaService,
                             DomainQueryService domainQueryService){
        this.panel = new CorsiDiLaureaPannello2(corsiDiLaureaService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}