package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello1;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.service.VerbaleService;
import javafx.scene.layout.VBox;

public class VerbaliView extends AbstractModelView<VerbaliPannello2> {

    public VerbaliView(VerbaleService verbaleService){
        this.panel = new VerbaliPannello2(verbaleService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }

    @Override
    protected VBox creaStatisticheContenuto() {
        // VBox vuota per ora
        return new VBox();
    }
    
}
