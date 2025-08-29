package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello1;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class VerbaliView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link VerbaliPannello2} per fornire una vista completa delle operazioni CRUD sui verbali.
     * </p>
     *
     * @return un {@link VBox} con la gestione dei verbali.
    */
    protected VBox creaGestioneContenuto(){
        return VerbaliPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link VerbaliPannello1} per visualizzare informazioni statistiche sui verbali.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche dei verbali.
    */
    protected VBox creaStatisticheContenuto(){
        return VerbaliPannello1.getView();
    }

    @Override
    public void onAdd(){
        VerbaliPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        VerbaliPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        VerbaliPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }
    
}
