package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello1;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;


public class EsamiView extends AbstractModelView<EsamiPannello1> {

    public EsamiView(EsameService esameService,
                     Supplier<List<Iscrizione>> loadIscrizioni,
                     DomainQueryService domainQueryService) {
        this.panel = new EsamiPannello1(esameService, loadIscrizioni, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
