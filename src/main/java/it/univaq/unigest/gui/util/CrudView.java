package it.univaq.unigest.gui.util;

/**
 * Contratto minimale per le viste CRUD.
 * <p>
 * Espone tre azioni standard — aggiungi, modifica, elimina — che possono
 * essere collegate a bottoni/shortcut e instradate verso il pannello CRUD
 * effettivo (vedi {@code CrudPanel}).
 * <p>
 */
public interface CrudView {

    /** Richiesta di apertura del dialog di inserimento/creazione. */
    void onAdd();

    /** Richiesta di modifica dell’elemento attualmente selezionato. */
    void onEdit();

    /** Richiesta di eliminazione dell’elemento attualmente selezionato. */
    void onDelete();
}