package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello1;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.IscrizioneService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;


public class IscrizioniView extends AbstractModelView<IscrizioniPannello2> {

    public IscrizioniView(IscrizioneService iscrizioneService,
                          Supplier<List<Studente>> loadStudenti,
                          Supplier<List<Appello>> loadAppelli){
        this.panel = new IscrizioniPannello2(iscrizioneService, loadStudenti, loadAppelli);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }
    
}
