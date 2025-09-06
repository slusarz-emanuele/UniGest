package it.univaq.unigest.service;

import it.univaq.unigest.model.Verbale;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti i {@link Verbale}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica dei verbali.
 */
public interface VerbaleService extends CrudService<Verbale, String> {

    /**
     * Filtra i verbali grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Verbale} che contiene i criteri di filtro
     * @return una {@link List} di {@link Verbale} che soddisfano i criteri
     */
    List<Verbale> filtra (Verbale filtro);
    
}
