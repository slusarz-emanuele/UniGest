package it.univaq.unigest.service;

import it.univaq.unigest.model.Edificio;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti gli {@link Edificio}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica degli edifici.
 */
public interface EdificioService extends CrudService<Edificio, String> {

    /**
     * Filtra gli edifici grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Edificio} che contiene i criteri di filtro
     * @return una {@link List} di {@link Edificio} che soddisfano i criteri
     */
    List<Edificio> filtra (Edificio filtro);
    
}
