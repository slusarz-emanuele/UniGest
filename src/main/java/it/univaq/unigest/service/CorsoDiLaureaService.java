package it.univaq.unigest.service;

import it.univaq.unigest.model.CorsoDiLaurea;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia dedicata alla gestione dei servizi riguardanti i {@link CorsoDiLaurea}.
 * Estende {@link CrudService} fornendo tutte le operazioni CRUD richieste
 * e i metodi aggiuntivi relativi alla logica dei corsi di laurea.
 */
public interface CorsoDiLaureaService extends CrudService<CorsoDiLaurea, String> {

    /**
     * Filtra i corsi di laurea grazie ai criteri indicati nell'oggetto {@code filtro}.
     * <p>
     *
     * @param filtro un oggetto {@link CorsoDiLaurea} che contiene i criteri di filtro
     * @return una {@link List} di {@link CorsoDiLaurea} che soddisfano i criteri
     */
    List<CorsoDiLaurea> filtra (CorsoDiLaurea filtro);

    /**
     * Restituisce la denominazione del corso di laurea corrispondente all'ID specificato.
     * In caso di ID nullo o inesistente, si ottiene una stringa vuota
     *
     * @param id l'identificativo del {@link CorsoDiLaurea}
     * @return il nome del corso di laurea, o una stringa vuota se non trovato.
     */
    default String labelById(String id) {
        return Optional.ofNullable(id)
                .flatMap(this::findById)
                .map(CorsoDiLaurea::getNome)
                .orElse("");
    }

}
