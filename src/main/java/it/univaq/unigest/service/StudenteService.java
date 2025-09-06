package it.univaq.unigest.service;

import it.univaq.unigest.model.Studente;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti gli {@link Studente}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica degli studenti.
 */
public interface StudenteService extends CrudService<Studente, String>{

    /**
     * Filtra gli studenti grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Studente} che contiene i criteri di filtro
     * @return una {@link List} di {@link Studente} che soddisfano i criteri
     */
    List<Studente> filtra (Studente filtro);

}
