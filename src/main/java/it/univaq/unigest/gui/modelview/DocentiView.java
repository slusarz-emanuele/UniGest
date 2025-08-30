package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello2;
import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.VBox;

public class DocentiView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link DocentiPannello2} per fornire una vista completa delle operazioni CRUD sui docenti.
     * </p>
     * 
     * @return un {@link VBox} con la gestione dei docenti.
     */
    protected VBox creaGestioneContenuto(){
        return DocentiPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link DocentiPannello1} per visualizzare informazioni statistiche sui docenti.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche dei docenti.
    */
    protected VBox creaStatisticheContenuto(){
        return DocentiPannello1.getView();
    }

    @Override
    public void onAdd(){
        DocentiPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        DocentiPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        DocentiPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getAppelloManager().salvaSuFile();
    }
    
}
