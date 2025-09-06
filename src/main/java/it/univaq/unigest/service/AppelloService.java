package it.univaq.unigest.service;

import it.univaq.unigest.model.Appello;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti gli {@link Appello}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica degli appelli.
 */
public interface AppelloService extends CrudService<Appello, String> {

    /**
     * Filtra gli appelli grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Appello} che ha corrispondenza con i criteri di filtro
     * @return una {@link List} di {@link Appello} che soddisfano i criteri
     */
    List<Appello> filtra (Appello filtro);
    
}
