package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello1;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

/**
 * Model-View per la gestione degli esami.
 * <p>
 * Incapsula il pannello {@link EsamiPannello1} e fornisce
 * la vista principale con tabella e dettagli degli esami.
 */
public class EsamiView extends AbstractModelView<EsamiPannello1> {

    /**
     * Costruisce un EsamiView.
     *
     * @param esameService servizio per la gestione degli esami
     * @param loadIscrizioni fornitore delle iscrizioni disponibili
     * @param domainQueryService servizio di query sul dominio
     */
    public EsamiView(EsameService esameService,
                     Supplier<List<Iscrizione>> loadIscrizioni,
                     DomainQueryService domainQueryService) {
        this.panel = new EsamiPannello1(esameService, loadIscrizioni, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
