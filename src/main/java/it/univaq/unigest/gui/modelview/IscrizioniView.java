package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello1;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;


public class IscrizioniView extends AbstractModelView<IscrizioniPannello1> {

    public IscrizioniView(IscrizioneService iscrizioneService,
                          Supplier<List<Studente>> loadStudenti,
                          Supplier<List<Appello>> loadAppelli,
                          DomainQueryService domainQueryService){
        this.panel = new IscrizioniPannello1(iscrizioneService, loadStudenti, loadAppelli, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }
    
}
