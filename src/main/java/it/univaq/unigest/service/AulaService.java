package it.univaq.unigest.service;

import it.univaq.unigest.model.Aula;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti le {@link Aula}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica delle aule.
 */
public interface AulaService extends CrudService<Aula, String> {

    /**
     * Filtra le aule grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Aula} che contiene i criteri di filtro
     * @return una {@link List} di {@link Aula} che soddisfano i criteri
     */
    List<Aula> filtra (Aula filtro);
    
}
