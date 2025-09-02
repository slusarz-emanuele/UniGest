package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.EdificioService;
import javafx.scene.layout.VBox;

public class EdificioView extends AbstractModelView<EdificiPannello2> {

    public EdificioView(EdificioService edificioService){
        this.panel = new EdificiPannello2(edificioService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }

}
