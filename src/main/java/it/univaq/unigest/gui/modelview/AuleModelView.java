package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello1;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.AulaService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

/**
 * Model-View per la gestione delle aule.
 * <p>
 * Incapsula il pannello {@link AulePannello1} e fornisce
 * la vista principale con tabella e dettagli delle aule.
 */
public class AuleModelView extends AbstractModelView<AulePannello1> {

    /**
     * Costruisce un AuleModelView.
     *
     * @param aulaService servizio per la gestione delle aule
     * @param loadEdifici loader per gli edifici disponibili
     * @param domainQueryService servizio di query sul dominio
     */
    public AuleModelView(AulaService aulaService,
                         Supplier<List<Edificio>> loadEdifici,
                         DomainQueryService domainQueryService) {
        this.panel = new AulePannello1(aulaService, loadEdifici, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}