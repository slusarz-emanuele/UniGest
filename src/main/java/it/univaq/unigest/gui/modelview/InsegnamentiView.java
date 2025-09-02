package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.InsegnamentoService;
import javafx.scene.layout.VBox;

public class InsegnamentiView extends AbstractModelView<InsegnamentiPannello2> {

    public InsegnamentiView(InsegnamentoService insegnamentoService){
        this.panel = new InsegnamentiPannello2(insegnamentoService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }
    
}
