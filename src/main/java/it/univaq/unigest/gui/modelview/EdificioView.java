package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello1;
import it.univaq.unigest.service.EdificioService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

/**
 * Model-View per la gestione degli edifici.
 * <p>
 * Incapsula il pannello {@link EdificiPannello1} e fornisce
 * la vista principale con tabella e dettagli degli edifici.
 */
public class EdificioView extends AbstractModelView<EdificiPannello1> {

    /**
     * Costruisce un EdificioView.
     *
     * @param edificioService servizio per la gestione degli edifici
     * @param domainQueryService servizio di query sul dominio
     */
    public EdificioView(EdificioService edificioService,
                        DomainQueryService domainQueryService){
        this.panel = new EdificiPannello1(edificioService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}
