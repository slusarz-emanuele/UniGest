package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello1;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.service.VerbaleService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class VerbaliView extends AbstractModelView<VerbaliPannello1> {

    public VerbaliView(VerbaleService verbaleService,
                       Supplier<List<Appello>> loadAppelli,
                       DomainQueryService domainQueryService){
        this.panel = new VerbaliPannello1(verbaleService, loadAppelli, domainQueryService);
    }

    @Override
    protected VBox creaGestioneContenuto() { return panel.getView(); }
    
}
