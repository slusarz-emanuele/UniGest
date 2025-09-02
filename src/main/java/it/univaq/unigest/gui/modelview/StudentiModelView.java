package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;

import it.univaq.unigest.service.StudenteService;
import javafx.scene.layout.*;

public class StudentiModelView extends AbstractModelView<StudentiPannello2> {

    public StudentiModelView (StudenteService studenteService){
        this.panel = new StudentiPannello2(studenteService);
    }

    @Override
    protected VBox creaGestioneContenuto() {
        return panel.getView();
    }

    @Override
    protected VBox creaStatisticheContenuto() {
        return new VBox();
    }
}
