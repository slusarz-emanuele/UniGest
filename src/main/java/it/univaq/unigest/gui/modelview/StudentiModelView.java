package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello1;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Model-View per la gestione degli studenti.
 * <p>
 * Incapsula il pannello {@link StudentiPannello1} e fornisce
 * la vista principale con tabella e dettagli degli studenti.
 */
public class StudentiModelView extends AbstractModelView<StudentiPannello1> {

     /**
     * Costruisce un StudentiModelView.
     *
     * @param studenteService servizio per la gestione degli studenti
     * @param loadCorsi fornitore dei corsi di laurea disponibili
     * @param domainQueryService servizio di query sul dominio
     */
    public StudentiModelView(StudenteService studenteService,
                             Supplier<List<CorsoDiLaurea>> loadCorsi,
                             DomainQueryService domainQueryService) {
        this.panel = new StudentiPannello1(studenteService, loadCorsi, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }
}
