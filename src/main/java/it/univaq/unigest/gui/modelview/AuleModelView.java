package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello2;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.AulaService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class AuleModelView extends AbstractModelView<AulePannello2> {

    public AuleModelView(AulaService aulaService,
                         Supplier<List<Edificio>> loadEdifici) {
        this.panel = new AulePannello2(aulaService, loadEdifici);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}