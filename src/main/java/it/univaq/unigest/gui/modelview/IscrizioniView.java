package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello1;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;


public class IscrizioniView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link IscrizioniPannello2} per fornire una vista completa delle operazioni CRUD sulle iscrizioni.
     * </p>
     * 
     * @return un {@link VBox} con la gestione delle iscrizioni.
    */
    protected VBox creaGestioneContenuto(){
        return IscrizioniPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link IscrizioniPannello1} per visualizzare informazioni statistiche sulle iscrizioni.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche delle iscrizioni.
    */
    protected VBox creaStatisticheContenuto(){
        return IscrizioniPannello1.getView();
    }

    @Override
    public void onAdd(){
        IscrizioniPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        IscrizioniPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        IscrizioniPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }
    
}
