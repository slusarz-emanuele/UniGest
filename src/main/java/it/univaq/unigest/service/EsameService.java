package it.univaq.unigest.service;

import it.univaq.unigest.model.Esame;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti gli {@link Esame}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica degli esami.
 */
public interface EsameService extends CrudService<Esame, String> {

    /**
     * Filtra gli esami grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Esame} che contiene i criteri di filtro
     * @return una {@link List} di {@link Esame} che soddisfano i criteri
     */
    List<Esame> filtra (Esame filtro);
    
}
