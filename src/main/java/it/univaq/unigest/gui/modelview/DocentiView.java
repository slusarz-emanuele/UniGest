package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello1;
import it.univaq.unigest.service.DocenteService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

/**
 * Model-View per la gestione dei docenti.
 * <p>
 * Incapsula il pannello {@link DocentiPannello1} e fornisce
 * la vista principale con tabella e dettagli dei docenti.
 */
public class DocentiView extends AbstractModelView<DocentiPannello1> {

     /**
     * Costruisce un DocentiView.
     *
     * @param docenteService servizio per la gestione dei docenti
     * @param domainQueryService servizio di query sul dominio
     */
    public DocentiView(DocenteService docenteService,
                       DomainQueryService domainQueryService) {
        this.panel = new DocentiPannello1(docenteService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
