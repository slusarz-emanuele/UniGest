package it.univaq.unigest.service;

import it.univaq.unigest.model.Docente;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti i {@link Docente}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica dei docenti.
 */
public interface DocenteService extends CrudService<Docente, String> {

    /**
     * Filtra i docenti grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Docente} che contiene i criteri di filtro
     * @return una {@link List} di {@link Docente} che soddisfano i criteri
     */
    List<Docente> filtra (Docente filtro);

    /**
     * Restituisce le generalità di un docente corrispondente al suo codice fiscale.
     * <p>
     *
     * @param cf il codice fiscale del {@link Docente}
     * @return la stringa con le generalità del docente, o una stringa vuota se non trovato
     */
    String getGeneralitaDaCf(String cf);

}
