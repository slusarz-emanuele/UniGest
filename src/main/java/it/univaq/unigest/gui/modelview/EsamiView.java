package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello2;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.service.AulaService;
import it.univaq.unigest.service.EsameService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;


public class EsamiView extends AbstractModelView<EsamiPannello2> {

    public EsamiView(EsameService esameService,
                     Supplier<List<Iscrizione>> loadIscrizioni) {
        this.panel = new EsamiPannello2(esameService, loadIscrizioni);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}
