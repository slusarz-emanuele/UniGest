package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;

import it.univaq.unigest.gui.util.CrudView;
import javafx.scene.layout.*;

/**
 * Implementazione concreta di {@link AbstractModelView} per la gestione e le statistiche degli studenti.
 * <p>
 * Fornisce:
 * <ul>
 *     <li>Una sezione "Gestione" con funzioni di inserimento, modifica ed eliminazione studenti
 *         tramite {@link StudentiPannello2}.</li>
 *     <li>Una sezione "Statistiche" con viste e analisi relative agli studenti
 *         tramite {@link StudentiPannello1}.</li>
 * </ul>
*/
public class StudentiModelView extends AbstractModelView implements CrudView {

    /**
     * Costruisce e restituisce il contenuto per la tab "Gestione".
     * <p>
     * Utilizza {@link StudentiPannello2} per fornire una vista completa delle operazioni CRUD sugli studenti.
     * </p>
     * 
     * @return un {@link VBox} con la gestione degli studenti.
    */
    protected VBox creaGestioneContenuto(){
        return StudentiPannello2.getView();
    }

    /**
     * Costruisce e restituisce il contenuto per la tab "Statistiche".
     * <p>
     * Utilizza {@link StudentiPannello1} per visualizzare informazioni statistiche sugli studenti.
     * </p>
     * 
     * @return un {@link VBox} con le statistiche degli studenti.
    */
    protected VBox creaStatisticheContenuto(){
        return StudentiPannello1.getView();
    }

    @Override
    public void onAdd(){
        StudentiPannello2.apriDialogAggiungi();
    }

    @Override
    public void onEdit(){
        StudentiPannello2.modificaSelezionato();
    }

    @Override
    public void onDelete(){
        StudentiPannello2.eliminaSelezionato();
    }

    @Override
    public void onSave(){
        Main.getStudenteManager().salvaSuFile();
    }
    
}
