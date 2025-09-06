package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.AppelloService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class AppelliModelView extends AbstractModelView<AppelliPannello2> {

    public AppelliModelView(AppelloService appelloService,
                            Supplier<List<Insegnamento>> loadInsegnamenti,
                            Supplier<List<Aula>> loadAula,
                            Supplier<List<Docente>> loadDocenti,
                            DomainQueryService domainQueryService) {
        this.panel = new AppelliPannello2(appelloService, loadInsegnamenti, loadAula, loadDocenti, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

}