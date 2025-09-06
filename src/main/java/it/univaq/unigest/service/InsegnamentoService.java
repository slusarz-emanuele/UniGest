package it.univaq.unigest.service;

import it.univaq.unigest.model.Insegnamento;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti gli {@link Insegnamento}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica degli insegnamenti.
 */
public interface InsegnamentoService extends CrudService<Insegnamento, String> {

    /**
     * Filtra gli insegnamenti grazie ai criteri indicati nell'oggetto {@code filtro}.
     * <p>
     *
     * @param filtro un oggetto {@link Insegnamento} che contiene i criteri di filtro
     * @return una {@link List} di {@link Insegnamento} che soddisfano i criteri
     */
    List<Insegnamento> filtra (Insegnamento filtro);
    
}
