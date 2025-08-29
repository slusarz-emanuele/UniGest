package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class EdificioView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link EdificiPannello2} per fornire una vista completa delle operazioni CRUD sugli edifici.
     * </p>
     * 
     * @return un {@link VBox} con la gestione degli edifici.
     */
    protected VBox creaGestioneContenuto(){
        return EdificiPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link EdificiPannello1} per visualizzare  informazioni statistiche sugli edifici.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche degli edifici.
     */
    protected VBox creaStatisticheContenuto(){
        return EdificiPannello1.getView();
    }

    @Override
    public void onAdd(){
        EdificiPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit() {
        EdificiPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete() {
        EdificiPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave() {
        Main.getAppelloManager().salvaSuFile();
    }

}
