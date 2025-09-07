package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello1;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.service.InsegnamentoService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

/**
 * Model-View per la gestione degli insegnamenti.
 * <p>
 * Incapsula il pannello {@link InsegnamentiPannello1} e fornisce
 * la vista principale con tabella e dettagli degli insegnamenti.
 */
public class InsegnamentiView extends AbstractModelView<InsegnamentiPannello1> {

    /**
     * Costruisce un InsegnamentiView.
     *
     * @param insegnamentoService servizio per la gestione degli insegnamenti
     * @param loadCorsi fornitore dei corsi di laurea disponibili
     * @param loadDocenti fornitore dei docenti disponibili
     * @param domainQueryService servizio di query sul dominio
     */
    public InsegnamentiView(InsegnamentoService insegnamentoService,
                            Supplier<List<CorsoDiLaurea>> loadCorsi,
                            Supplier<List<Docente>> loadDocenti,
                            DomainQueryService domainQueryService){
        this.panel = new InsegnamentiPannello1(insegnamentoService, loadCorsi, loadDocenti, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }
    
}
