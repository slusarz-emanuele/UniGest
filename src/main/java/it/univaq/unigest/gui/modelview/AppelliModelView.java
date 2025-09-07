package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello1;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.AppelloService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

/**
 * Model-View per la gestione degli appelli.
 * <p>
 * Incapsula il pannello {@link AppelliPannello1} e fornisce
 * la vista principale con tabella e dettagli degli appelli.
 */
public class AppelliModelView extends AbstractModelView<AppelliPannello1> {

     /**
     * Costruisce un AppelliModelView.
     *
     * @param appelloService servizio per la gestione degli appelli
     * @param loadInsegnamenti loader per gli insegnamenti disponibili
     * @param loadAula loader per le aule disponibili
     * @param loadDocenti loader per i docenti disponibili
     * @param domainQueryService servizio di query sul dominio
     */
    public AppelliModelView(AppelloService appelloService,
                            Supplier<List<Insegnamento>> loadInsegnamenti,
                            Supplier<List<Aula>> loadAula,
                            Supplier<List<Docente>> loadDocenti,
                            DomainQueryService domainQueryService) {
        this.panel = new AppelliPannello1(appelloService, loadInsegnamenti, loadAula, loadDocenti, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}