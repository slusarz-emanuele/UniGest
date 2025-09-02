package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;
import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.service.StudenteService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class StudentiModelView extends AbstractModelView<StudentiPannello2> {

    public StudentiModelView(StudenteService studenteService,
                             Supplier<List<CorsoDiLaurea>> loadCorsi,
                             Function<String, String> nomeCdlById) {
        this.panel = new StudentiPannello2(studenteService, loadCorsi, nomeCdlById);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() { return new VBox(); }
}
