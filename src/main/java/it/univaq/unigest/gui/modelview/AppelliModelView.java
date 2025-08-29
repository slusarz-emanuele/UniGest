package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello1;
import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;

import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;
import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class AppelliModelView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link AppelliPannello2} per fornire una vista completa delle operazioni CRUD sugli appelli.
     * </p>
     * 
     * @return un {@link VBox} con la gestione degli appelli.
     */
    protected VBox creaGestioneContenuto(){
        return AppelliPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link AppelliPannello1} per visualizzare le informazioni statistiche sugli appelli.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche degli appelli.
     */
    protected VBox creaStatisticheContenuto(){
        return AppelliPannello1.getView();
    }

    @Override
    public void onAdd(){
        AppelliPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        AppelliPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        AppelliPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }
}