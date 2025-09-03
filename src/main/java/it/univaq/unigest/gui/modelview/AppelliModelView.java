package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.AppelloService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class AppelliModelView extends AbstractModelView<AppelliPannello2> {

    public AppelliModelView(AppelloService appelloService,
                            Supplier<List<Insegnamento>> loadInsegnamenti,
                            Supplier<List<Aula>> loadAula,
                            Supplier<List<Docente>> loadDocenti) {
        this.panel = new AppelliPannello2(appelloService, loadInsegnamenti, loadAula, loadDocenti);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}