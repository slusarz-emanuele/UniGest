package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class InsegnamentiView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link InsegnamentiPannello2} per fornire una vista completa delle operazioni CRUD sugli insegnamenti.
     * </p>
     * 
     * @return un {@link VBox} con la gestione degli insegnamenti.
     */
    protected VBox creaGestioneContenuto(){
        return InsegnamentiPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link InsegnamentiPannello1} per visualizzare informazioni statistiche sugli insegnamenti.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche degli insegnamenti.
     */
    protected VBox creaStatisticheContenuto(){
        return InsegnamentiPannello1.getView();
    }

    @Override
    public void onAdd(){
        InsegnamentiPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        InsegnamentiPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        InsegnamentiPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }
    
}
