package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello1;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class CorsoDiLaureaView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link CorsiDiLaureaPannello2} per fornire una vista completa delle operazioni CRUD sui corsi di laurea.
     * </p>
     * 
     * @return un {@link VBox} con la gestione dei corsi di laurea.
    */
    protected VBox creaGestioneContenuto(){
        return CorsiDiLaureaPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link CorsiDiLaureaPannello1} per visualizzare informazioni statistiche sui corsi di laurea.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche dei corsi di laurea.
    */
    protected VBox creaStatisticheContenuto(){
        return CorsiDiLaureaPannello1.getView();
    }

    @Override
    public void onAdd(){
        CorsiDiLaureaPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        CorsiDiLaureaPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        CorsiDiLaureaPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }

}