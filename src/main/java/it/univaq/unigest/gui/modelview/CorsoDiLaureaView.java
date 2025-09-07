package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello1;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

/**
 * Model-View per la gestione dei corsi di laurea.
 * <p>
 * Incapsula il pannello {@link CorsiDiLaureaPannello1} e fornisce
 * la vista principale con tabella e dettagli dei corsi di laurea.
 */
public class CorsoDiLaureaView extends AbstractModelView<CorsiDiLaureaPannello1> {

    /**
     * Costruisce un CorsoDiLaureaView.
     *
     * @param corsiDiLaureaService servizio per la gestione dei corsi di laurea
     * @param domainQueryService servizio di query sul dominio
     */
    public CorsoDiLaureaView(CorsoDiLaureaService corsiDiLaureaService,
                             DomainQueryService domainQueryService){
        this.panel = new CorsiDiLaureaPannello1(corsiDiLaureaService, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}