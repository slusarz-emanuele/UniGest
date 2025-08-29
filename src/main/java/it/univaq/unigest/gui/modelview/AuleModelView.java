package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello1;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class AuleModelView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link AulePannello2} per fornire una vista completa delle operazioni CRUD sulle aule.
     * </p>
     * 
     * @return un {@link VBox} con la gestione delle aule.
     */
    protected VBox creaGestioneContenuto(){
        return AulePannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link AulePannello1} per visualizzare informazioni statistiche sulle aule.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche delle aule.
     */
    protected VBox creaStatisticheContenuto(){
        return AulePannello1.getView();
    }

    @Override
    public void onAdd(){
        AulePannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        AulePannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        AulePannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }

}