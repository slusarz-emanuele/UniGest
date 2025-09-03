package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello2;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.service.InsegnamentoService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class InsegnamentiView extends AbstractModelView<InsegnamentiPannello2> {

    public InsegnamentiView(InsegnamentoService insegnamentoService,
                            Supplier<List<CorsoDiLaurea>> loadCorsi,
                            Supplier<List<Docente>> loadDocenti){
        this.panel = new InsegnamentiPannello2(insegnamentoService, loadCorsi, loadDocenti);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }
    
}
