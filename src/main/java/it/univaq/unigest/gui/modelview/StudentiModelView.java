package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class StudentiModelView extends AbstractModelView<StudentiPannello2> {

    public StudentiModelView(StudenteService studenteService,
                             Supplier<List<CorsoDiLaurea>> loadCorsi,
                             Function<String, String> nomeCdlById,
                             DomainQueryService domainQueryService) {
        this.panel = new StudentiPannello2(studenteService, loadCorsi, nomeCdlById, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }
}
