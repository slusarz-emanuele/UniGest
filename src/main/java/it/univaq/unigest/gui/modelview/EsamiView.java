package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.model.Esame;
import javafx.scene.layout.VBox;


public class EsamiView extends AbstractModelView<Esame> {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link EsamiPannello2} per fornire una vista completa delle operazioni CRUD sugli esami.
     * </p>
     * 
     * @return un {@link VBox} con la gestione degli esami.
    */
    protected VBox creaGestioneContenuto(){
        return EsamiPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link EsamiPannello1} per visualizzare informazioni statistiche sugli esami.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche degli esami.
    */
    protected VBox creaStatisticheContenuto(){
        return EsamiPannello1.getView();
    }

    @Override
    public void onAdd(){
        EsamiPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        EsamiPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        EsamiPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }

}
