package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello1;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello2;

import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.service.AulaService;
import it.univaq.unigest.service.CorsoDiLaureaService;
import javafx.scene.layout.VBox;

public class AuleModelView extends AbstractModelView<AulePannello2> {

    public AuleModelView(AulaService aulaService){
        this.panel = new AulePannello2(aulaService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}