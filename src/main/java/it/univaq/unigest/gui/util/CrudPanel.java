package it.univaq.unigest.gui.util;

import javafx.scene.layout.VBox;

/**
 * Contratto per i pannelli CRUD (Create, Read, Update, Delete) usati all’interno delle viste.
 * <p>
 * Le implementazioni forniscono:
 * <ul>
 *   <li>il nodo principale da montare nell’UI ({@link #getView()});</li>
 *   <li>l’apertura del dialog di inserimento ({@link #apriDialogAggiungiPubblico()});</li>
 *   <li>la modifica dell’elemento selezionato ({@link #modificaSelezionato()});</li>
 *   <li>l’eliminazione dell’elemento selezionato ({@link #eliminaSelezionato()});</li>
 *   <li>il refresh dei dati ({@link #refresh()}).</li>
 * </ul>
 * <p>
 * Nota di design: mantiene separata la logica UI dalla logica di dominio/servizi.
 */
public interface CrudPanel {

    /** Restituisce il contenitore principale del pannello. */
    VBox getView();

    /** Apre il dialog di inserimento/creazione. */
    void apriDialogAggiungiPubblico();

    /** Avvia la modifica dell’elemento attualmente selezionato. */
    void modificaSelezionato();

    /** Elimina l’elemento attualmente selezionato (con eventuali conferme/validazioni). */
    void eliminaSelezionato();

    /** Ricarica i dati (es. dopo CRUD riuscita). */
    void refresh();
}
