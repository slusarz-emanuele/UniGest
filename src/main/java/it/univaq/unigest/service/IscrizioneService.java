package it.univaq.unigest.service;

import it.univaq.unigest.model.Iscrizione;

import java.util.List;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti le {@link Iscrizione}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica delle iscrizioni.
 */
public interface IscrizioneService extends CrudService<Iscrizione, String> {

    /**
     * Filtra le iscrizioni grazie ai criteri indicati nell'oggetto {@code filtro}.
     *
     * @param filtro un oggetto {@link Iscrizione} che contiene i criteri di filtro
     * @return una {@link List} di {@link Iscrizione} che soddisfano i criteri
     */
    List<Iscrizione> filtra (Iscrizione filtro);
    
}
